package cstjean.mobile2.demofilms.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cstjean.mobile2.demofilms.database.dao.FilmDao
import cstjean.mobile2.demofilms.database.dao.RealisateurDao
import cstjean.mobile2.demofilms.database.entity.FilmEntity
import cstjean.mobile2.demofilms.database.entity.RealisateurEntity

@Database(
    entities = [
        FilmEntity::class,
        RealisateurEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
abstract class FilmDatabase : RoomDatabase() {

    abstract fun filmDao(): FilmDao
    abstract fun realisateurDao(): RealisateurDao

    companion object {
        @Volatile
        private var INSTANCE: FilmDatabase? = null

        fun getInstance(context: Context): FilmDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FilmDatabase::class.java,
                    "films_database.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
