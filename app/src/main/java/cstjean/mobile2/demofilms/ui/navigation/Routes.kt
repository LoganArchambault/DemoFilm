package cstjean.mobile2.demofilms.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object FilmsRoute

@Serializable
object AboutRoute

@Serializable
data class DetailsRoute(val idFilm: Int)

@Serializable
object CreateFilmRoute