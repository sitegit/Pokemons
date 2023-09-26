package com.example.pokemons.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.pokemons.data.model.PokeEntryDbModel

@Dao
interface PokeEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokeEntries: List<PokeEntryDbModel>)

    @Query("SELECT * FROM poke_entries WHERE name LIKE :query")
    fun searchPokemons(query: String): PagingSource<Int, PokeEntryDbModel>

    @Query("SELECT * FROM poke_entries")
    fun getPokemons(): PagingSource<Int, PokeEntryDbModel>

    @Query("DELETE FROM poke_entries")
    suspend fun clearAll()

    @Transaction
    suspend fun refresh(launches: List<PokeEntryDbModel>) {
        clearAll()
        insertAll(launches)
    }

}