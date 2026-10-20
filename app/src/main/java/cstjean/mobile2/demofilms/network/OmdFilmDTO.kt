package cstjean.mobile2.demofilms.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OmdbFilmDto(
    @SerialName("Title") val titre: String,
    @SerialName("Year") val annee: String,
    @SerialName("Director") val realisateur: String? = null,
    @SerialName("imdbRating") val noteImdb: String? = null,
    // Pas nécessaire actuellement, mais on pourrait penser utiliser l'info plus tard
    @SerialName("Poster") val afficheUrl: String? = null
)