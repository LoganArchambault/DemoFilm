package cstjean.mobile2.demofilms.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import cstjean.mobile2.demofilms.database.entity.FilmEntity
import cstjean.mobile2.demofilms.database.entity.RealisateurEntity

data class FilmAvecRealisateur(
    @Embedded
    val film: FilmEntity,

    @Relation(
        parentColumn = "realisateurId",
        entityColumn = "realisateurId"
    )
    val realisateur: RealisateurEntity?
)