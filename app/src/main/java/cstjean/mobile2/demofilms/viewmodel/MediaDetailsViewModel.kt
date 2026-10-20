package cstjean.mobile2.demofilms.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cstjean.mobile2.demofilms.repository.FakeFilmRepository
import cstjean.mobile2.demofilms.repository.IFilmRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MediaDetailsViewModel(
    private val repository: IFilmRepository = FakeFilmRepository(),
    private val idFilm: Int
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(MediaDetailsUiState())

    val uiState =
        _uiState.asStateFlow()

    init {
        chargerFilm()
    }

    fun supprimerFilm() {
        viewModelScope.launch {
            repository.supprimerFilm(idFilm)
        }
    }

    private fun chargerFilm() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val film =
                    repository.getDetailsFilm(idFilm)

                _uiState.update {
                    it.copy(
                        film = film,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        erreur = e.message
                    )
                }
            }
        }
    }
}
