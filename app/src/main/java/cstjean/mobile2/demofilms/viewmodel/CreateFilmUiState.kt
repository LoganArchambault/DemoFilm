package cstjean.mobile2.demofilms.viewmodel

data class CreateFilmUiState(
    val titre: String = "",
    val annee: String = "",
    val note: String = "",
    val nomRealisateur: String = "",
    val erreurTitre: String? = null,
    val erreurAnnee: String? = null,
    val erreurNote: String? = null,
    val isSaving: Boolean = false,
    val erreur: String? = null
)