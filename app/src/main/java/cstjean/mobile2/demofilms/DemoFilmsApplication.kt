package cstjean.mobile2.demofilms

import android.app.Application
import cstjean.mobile2.demofilms.database.FilmDatabase
import cstjean.mobile2.demofilms.network.RetrofitInstance
import cstjean.mobile2.demofilms.repository.RoomFilmRepository
import kotlin.getValue

class DemoFilmsApplication : Application() {
    val database by lazy { FilmDatabase.getInstance(this) }

    // Récupération de l'API et invocation du constructeur
    val omdbApiService by lazy { RetrofitInstance.api }
    val repository by lazy { RoomFilmRepository(
        database.filmDao(),
        database.realisateurDao(),
        omdbApiService
    )}
}
