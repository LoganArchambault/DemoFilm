package cstjean.mobile2.demofilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cstjean.mobile2.demofilms.BuildConfig
import cstjean.mobile2.demofilms.data.Film
import cstjean.mobile2.demofilms.repository.FakeFilmRepository
import cstjean.mobile2.demofilms.repository.IFilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TriOption { AUCUN, TITRE, NOTE }

class MediaViewModel(private val repository: IFilmRepository) : ViewModel() {
    // On conserve l'état du tri et du film sélectionné dans des StateFlow locaux
    private val _triOption = MutableStateFlow(TriOption.AUCUN)
    private val _filmSelectionne = MutableStateFlow<Film?>(null)
    private val _errorMessage = MutableStateFlow<String?>(null)
    private val _rechercheOmdb = MutableStateFlow("")
    private val _isSearching = MutableStateFlow(false)
    private val _rechercheState = combine(_rechercheOmdb, _isSearching) { query, isSearching ->
        query to isSearching
    }

    // On combine le Flow de Room avec nos états locaux de tri et de sélection
    val uiState: StateFlow<MediaUiState> = combine(
        repository.observerFilms().catch { e ->
            _errorMessage.value = e.message
            emit(emptyList())
        },
        _triOption,
        _filmSelectionne,
        _errorMessage,
        _rechercheState
    ) { films, tri, selectionne, error, (query, isSearching) ->

        val filmsTries = when (tri) {
            TriOption.TITRE -> films.sortedBy { it.titre }
            TriOption.NOTE -> films.sortedByDescending { it.note }
            TriOption.AUCUN -> films
        }

        MediaUiState(
            films = filmsTries,
            filmSelectionne = selectionne,
            isLoading = false,
            erreur = error,
            rechercheOmdb = query,
            isSearching = isSearching
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MediaUiState(isLoading = true)
    )


    fun selectionnerFilm(film: Film) {
        _filmSelectionne.value = film
        println("Film sélectionné: ${film.titre}")
    }

    fun trierParTitre() {
        _triOption.value = TriOption.TITRE
    }

    fun trierParNote() {
        _triOption.value = TriOption.NOTE
    }
    fun onRechercheOmdbChange(nouvelleQuery: String) {
        _rechercheOmdb.value = nouvelleQuery
        // Optionnel : Réinitialiser le message d'erreur quand l'utilisateur retape
        _errorMessage.value = null
    }

    fun importerFilm() {
        val query = _rechercheOmdb.value.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _isSearching.value = true
            _errorMessage.value = null

            // Récupération de la clé du fichier local.properties
            val result = repository.importerFilmDepuisApi(query, BuildConfig.OMDBAPI_KEY)

            result.onSuccess {
                // Réinitialiser la barre de recherche en cas de succès
                _rechercheOmdb.value = ""
            }.onFailure { exception ->
                _errorMessage.value = /*exception.message ?:*/ "Erreur lors de l'importation."
            }

            _isSearching.value = false
        }
    }

    fun effacerErreur() {
        _errorMessage.value = null
    }
}
