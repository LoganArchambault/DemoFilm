package cstjean.mobile2.demofilms.repository

import cstjean.mobile2.demofilms.data.Film
import cstjean.mobile2.demofilms.database.dao.FilmDao
import cstjean.mobile2.demofilms.database.toEntity
import cstjean.mobile2.demofilms.database.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import cstjean.mobile2.demofilms.database.dao.RealisateurDao
import cstjean.mobile2.demofilms.database.entity.RealisateurEntity
import cstjean.mobile2.demofilms.network.OmdbApiService
import retrofit2.HttpException
import java.io.IOException

class RoomFilmRepository(
    private val filmDao: FilmDao,
    private val realisateurDao: RealisateurDao,
    // Nouveau service ajouté
    private val apiService: OmdbApiService
) : IFilmRepository {
    override suspend fun chargerFilms(): List<Film> {
        return filmDao.chargerTousLesFilms().map { entity ->
            entity.toModel()
        }
    }

    override fun observerFilms(): Flow<List<Film>> {
        return filmDao.obtenirTousLesFilms().map { listeEntities ->
            listeEntities.map { entity -> entity.toModel() }
        }
    }

    override suspend fun trouverFilm(id: Int): Film? {
        return filmDao.trouverParId(id)?.toModel()
    }

    override suspend fun supprimerFilm(id: Int) {
        filmDao.supprimerParId(id)
    }

    override suspend fun ajouterFilm(film: Film) {
        filmDao.insererFilm(film.toEntity())
    }
    override suspend fun sauvegarderFilmAvecRealisateur(
        titre: String,
        annee: Int,
        note: Double,
        nomRealisateur: String
    ) {
        val nom = nomRealisateur.trim()

        val realisateurId: Long? = if (nom.isNotEmpty()) {

            val existant =
                realisateurDao.getRealisateurParNom(nom)

            existant?.realisateurId
                ?: realisateurDao.insererRealisateur(
                    RealisateurEntity(nom = nom)
                )

        } else {
            null
        }

        val nouveauFilm =
            cstjean.mobile2.demofilms.database.entity.FilmEntity(
                titre = titre,
                annee = annee,
                note = note,
                realisateurId = realisateurId
            )

        filmDao.insererFilm(nouveauFilm)
    }

    override suspend fun getDetailsFilm(
        filmId: Int
    ): Film? {
        return filmDao
            .getFilmAvecRealisateur(filmId)
            ?.toModel()
    }
    override suspend fun importerFilmDepuisApi(titre: String, apiKey: String): Result<Unit> {
        return try {
            // 1. Appel réseau
            val dto = apiService.chercherFilm(titre, apiKey)

            // 2. Extraction et nettoyage des données du DTO
            val titreFilm = dto.titre
            val anneeFilm = dto.annee.take(4).toIntOrNull() ?: 2024
            val noteFilm = dto.noteImdb?.toDoubleOrNull() ?: 0.0

            // OMDb sépare parfois les réalisateurs par une virgule, ou renvoie "N/A"
            val realisateur = dto.realisateur?.trim()
            val nomRealisateur = if (!realisateur.isNullOrEmpty() && realisateur != "N/A") {
                realisateur.split(",").first().trim()
            } else {
                ""
            }

            // 3. Réutilisation de la méthode existante !
            sauvegarderFilmAvecRealisateur(
                titre = titreFilm,
                annee = anneeFilm,
                note = noteFilm,
                nomRealisateur = nomRealisateur
            )

            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(Exception("Veuillez vérifier votre connexion Internet."))
        } catch (e: HttpException) {
            Result.failure(Exception("Erreur du serveur d'API : ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
