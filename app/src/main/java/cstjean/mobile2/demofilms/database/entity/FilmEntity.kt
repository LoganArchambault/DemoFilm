package cstjean.mobile2.demofilms.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "films",
    foreignKeys = [
        ForeignKey(
            entity = RealisateurEntity::class,
            parentColumns = ["realisateurId"],
            childColumns = ["realisateurId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["realisateurId"])]
)
data class FilmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titre: String,
    val annee: Int,
    val note: Double,
    val realisateurId: Long?
)