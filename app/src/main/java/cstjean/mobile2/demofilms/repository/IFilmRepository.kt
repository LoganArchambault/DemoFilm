package cstjean.mobile2.demofilms.repository

import cstjean.mobile2.demofilms.data.Film
import kotlinx.coroutines.flow.Flow

interface IFilmRepository {

    suspend fun chargerFilms(): List<Film>

    fun observerFilms(): Flow<List<Film>>

    suspend fun trouverFilm(id: Int): Film?

    suspend fun supprimerFilm(id: Int)

    suspend fun ajouterFilm(film: Film)

    suspend fun sauvegarderFilmAvecRealisateur(
        titre: String,
        annee: Int,
        note: Double,
        nomRealisateur: String
    )

    suspend fun getDetailsFilm(
        filmId: Int
    ): Film?

    suspend fun importerFilmDepuisApi(titre: String, apiKey: String): Result<Unit>
}