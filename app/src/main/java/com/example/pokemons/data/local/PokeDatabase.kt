package com.example.pokemons.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pokemons.data.model.PokeEntryDbModel

@Database(entities = [PokeEntryDbModel::class], version = 17, exportSchema = false)
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

    abstract fun pokeEntryDao(): PokeEntryDao
}
