package cstjean.mobile2.demofilms.data

data class Film(
    val id: Int,
    val titre: String,
    val annee: Int,
    val note: Double,
    val nomRealisateur: String? = null
)
