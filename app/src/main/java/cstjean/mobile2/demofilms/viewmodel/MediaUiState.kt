package cstjean.mobile2.demofilms.viewmodel

import cstjean.mobile2.demofilms.data.Film

data class MediaUiState(
    val films: List<Film> = emptyList(),
    val filmSelectionne: Film? = null,
    val isLoading: Boolean = false,
    val erreur: String? = null,
    val rechercheOmdb: String = "",
    val isSearching: Boolean = false
)