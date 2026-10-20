package cstjean.mobile2.demofilms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cstjean.mobile2.demofilms.repository.FakeFilmRepository
import cstjean.mobile2.demofilms.repository.IFilmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateFilmViewModel(
    private val repository: IFilmRepository = FakeFilmRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateFilmUiState())

    val uiState = _uiState.asStateFlow()

    fun modifierTitre(titre: String) {
        _uiState.update {
            it.copy(
                titre = titre,
                erreurTitre = null
            )
        }
    }

    fun modifierAnnee(annee: String) {
        _uiState.update {
            it.copy(
                annee = annee,
                erreurAnnee = null
            )
        }
    }

    fun modifierNote(note: String) {
        _uiState.update {
            it.copy(
                note = note,
                erreurNote = null
            )
        }
    }

    fun modifierNomRealisateur(
        nomRealisateur: String
    ) {
        _uiState.update {
            it.copy(
                nomRealisateur = nomRealisateur
            )
        }
    }

    fun creerFilm(onSuccess: () -> Unit) {
        val state = _uiState.value

        var formulaireValide = true

        if (state.titre.isBlank()) {
            _uiState.update { it.copy(erreurTitre = "Le titre est obligatoire") }
            formulaireValide = false
        }

        if (state.annee.isBlank()) {
            _uiState.update { it.copy(erreurAnnee = "L'année est obligatoire") }
            formulaireValide = false
        }

        if (state.note.isBlank()) {
            _uiState.update { it.copy(erreurNote = "La note est obligatoire") }
            formulaireValide = false
        }

        if (!formulaireValide) return

        val annee = state.annee.toIntOrNull()
        val note = state.note.toDoubleOrNull()

        if (annee == null) {
            _uiState.update {
                it.copy(erreurAnnee = "L'année doit être un nombre")
            }
            return
        }

        if (note == null) {
            _uiState.update {
                it.copy(erreurNote = "La note doit être un nombre")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, erreur = null) }

            try {
                repository.sauvegarderFilmAvecRealisateur(
                    titre = state.titre,
                    annee = annee,
                    note = note,
                    nomRealisateur = state.nomRealisateur
                )

                onSuccess()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        erreur = e.message
                    )
                }
            }
        }
    }
}
