package cstjean.mobile2.demofilms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cstjean.mobile2.demofilms.database.entity.RealisateurEntity

@Dao
interface RealisateurDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insererRealisateur(
        realisateur: RealisateurEntity
    ): Long

    @Query(
        "SELECT * FROM realisateurs " +
                "WHERE LOWER(nom) = LOWER(:nom) LIMIT 1"
    )
    suspend fun getRealisateurParNom(
        nom: String
    ): RealisateurEntity?
}