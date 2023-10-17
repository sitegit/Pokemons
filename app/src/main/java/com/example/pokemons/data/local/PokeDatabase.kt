package com.example.pokemons.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.model.PokeFavouriteDb
import com.example.pokemons.data.model.PokeInfoDb

@Database(entities = [PokeEntryDb::class, PokeInfoDb::class, PokeFavouriteDb::class], version = 38, exportSchema = false)
@TypeConverters(PokeTypeConverter::class)
abstract class PokeDatabase : RoomDatabase() {
    companion object {

        private var db: PokeDatabase? = null
        private const val DB_NAME = "main.db"
        private val LOCK = Any()

        fun getInstance(context: Context): PokeDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance =
                    Room.databaseBuilder(
                        context,
                        PokeDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun pokeEntryDao(): PokeDao
}
