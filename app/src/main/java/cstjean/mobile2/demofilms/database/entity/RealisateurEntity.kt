package cstjean.mobile2.demofilms.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "realisateurs",
    indices = [Index(value = ["nom"], unique = true)]
)
data class RealisateurEntity(
    @PrimaryKey(autoGenerate = true)
    val realisateurId: Long = 0,
    val nom: String
)