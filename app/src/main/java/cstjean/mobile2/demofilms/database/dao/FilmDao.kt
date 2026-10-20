package cstjean.mobile2.demofilms.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cstjean.mobile2.demofilms.database.entity.FilmEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.Transaction
import cstjean.mobile2.demofilms.database.relation.FilmAvecRealisateur

@Dao
interface FilmDao {
    @Query("SELECT * FROM films ORDER BY titre ASC")
    fun obtenirTousLesFilms(): Flow<List<FilmEntity>>

    @Query("SELECT * FROM films WHERE id = :id")
    suspend fun trouverParId(id: Int): FilmEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insererFilm(film: FilmEntity)

    @Update
    suspend fun modifierFilm(film: FilmEntity)

    @Delete
    suspend fun supprimerFilm(film: FilmEntity)

    @Query("DELETE FROM films WHERE id = :id")
    suspend fun supprimerParId(id: Int)

    @Query("SELECT * FROM films WHERE id = :id")
    fun observerParId(id: Int): Flow<FilmEntity?>

    @Query("SELECT * FROM films ORDER BY titre ASC")
    suspend fun chargerTousLesFilms(): List<FilmEntity>

    @Transaction
    @Query("SELECT * FROM films WHERE id = :filmId")
    suspend fun getFilmAvecRealisateur(
        filmId: Int
    ): FilmAvecRealisateur?

    @Transaction
    @Query("SELECT * FROM films")
    fun obtenirTousLesFilmsAvecRealisateur():
            Flow<List<FilmAvecRealisateur>>
}