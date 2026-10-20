package cstjean.mobile2.demofilms.viewmodel

import cstjean.mobile2.demofilms.data.Film

data class MediaDetailsUiState(
    val film: Film? = null,
    val isLoading: Boolean = false,
    val erreur: String? = null
)
