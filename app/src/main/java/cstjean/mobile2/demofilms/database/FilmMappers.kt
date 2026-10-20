package cstjean.mobile2.demofilms.database

import cstjean.mobile2.demofilms.data.Film
import cstjean.mobile2.demofilms.database.entity.FilmEntity
import cstjean.mobile2.demofilms.database.relation.FilmAvecRealisateur
import cstjean.mobile2.demofilms.network.OmdbFilmDto

fun FilmEntity.toModel(): Film {
    return Film(
        id = this.id,
        titre = this.titre,
        annee = this.annee,
        note = this.note,
        nomRealisateur = null
    )
}

fun Film.toEntity(): FilmEntity {
    return FilmEntity(
        id = this.id,
        titre = this.titre,
        annee = this.annee,
        note = this.note,
        realisateurId = null
    )
}

fun FilmAvecRealisateur.toModel(): Film {
    return Film(
        id = this.film.id,
        titre = this.film.titre,
        annee = this.film.annee,
        note = this.film.note,
        nomRealisateur = this.realisateur?.nom
    )
}

// Mapper : Convertit le DTO réseau vers l'entité Room
fun OmdbFilmDto.toEntity(realisateurId: Long?): FilmEntity {
    return FilmEntity(
        id = 0, // Auto-généré par Room
        titre = this.titre,
        annee = this.annee.take(4).toIntOrNull() ?: 2024,
        note = this.noteImdb?.toDoubleOrNull() ?: 0.0,
        realisateurId = realisateurId // Clé étrangère résolue au préalable
    )
}
