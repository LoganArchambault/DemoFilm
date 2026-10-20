package cstjean.mobile2.demofilms.repository

import cstjean.mobile2.demofilms.data.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class FakeFilmRepository : IFilmRepository {
    override suspend fun chargerFilms(): List<Film> = withContext(Dispatchers.IO) {
        delay(3000.milliseconds)
        FakeFilmData.fullList
    }

    override fun observerFilms(): Flow<List<Film>> = flow {
        emit(FakeFilmData.fullList.take(3))
        delay(2000.milliseconds)
        emit(FakeFilmData.fullList)
    }.flowOn(Dispatchers.IO)

    override suspend fun trouverFilm(id: Int): Film? =
        withContext(Dispatchers.IO) {

            delay(1000.milliseconds)

            FakeFilmData.fullList
                .firstOrNull { it.id == id }
        }

    override suspend fun supprimerFilm(id: Int) {
        withContext(Dispatchers.IO) {
            val film = FakeFilmData.fullList.firstOrNull { it.id == id }
            if (film != null) {
                FakeFilmData.fullList.remove(film)
            } else {
                throw Exception("Film introuvable")
            }
        }
    }

    override suspend fun ajouterFilm(film: Film) {
        TODO("Not yet implemented")
    }
    override suspend fun sauvegarderFilmAvecRealisateur(
        titre: String,
        annee: Int,
        note: Double,
        nomRealisateur: String
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailsFilm(
        filmId: Int
    ): Film? {
        TODO("Not yet implemented")
    }

    override suspend fun importerFilmDepuisApi(
        titre: String,
        apiKey: String
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

}

object FakeFilmData {
    val fullList = mutableListOf(
        Film(1, "The Shawshank Redemption", 1994, 9.3),
        Film(2, "The Godfather", 1972, 9.2),
        Film(3, "The Dark Knight", 2008, 9.0),
        Film(4, "12 Angry Men", 1957, 9.0),
        Film(5, "Schindler's List", 1993, 8.9),
        Film(6, "The Lord of the Rings: The Return of the King", 2003, 8.9),
        Film(7, "Pulp Fiction", 1994, 8.9),
        Film(8, "The Good, the Bad and the Ugly", 1966, 8.8),
        Film(9, "Fight Club", 1999, 8.8),
        Film(10, "Forrest Gump", 1994, 8.8)
    )
}