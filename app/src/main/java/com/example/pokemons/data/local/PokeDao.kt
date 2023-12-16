package com.example.pokemons.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.model.PokeFavouriteDb
import com.example.pokemons.data.model.PokeInfoDb

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokeEntries: List<PokeEntryDb>)

    @Query("SELECT * FROM poke_entries WHERE name LIKE :query")
    fun searchPokemons(query: String): PagingSource<Int, PokeEntryDb>

    @Query("SELECT * FROM poke_entries")
    fun getPokemons(): PagingSource<Int, PokeEntryDb>

    @Query("DELETE FROM poke_entries")
    suspend fun clearAll()

    @Transaction
    suspend fun refresh(launches: List<PokeEntryDb>) {
        clearAll()
        insertAll(launches)
    }

    //info
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInfo(pokeInfo: List<PokeInfoDb>)

    @Query("SELECT * FROM poke_info WHERE name = :name")
    suspend fun getPokemon(name: String): PokeInfoDb

    //favorite
    @Query("SELECT * FROM poke_favourite")
    fun getAllFavouritePokemons(): LiveData<List<PokeFavouriteDb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouritePokemon(favouritePokemon: PokeFavouriteDb)

    @Delete
    suspend fun deletePokemon(favouritePokemon: PokeFavouriteDb)

    @Query("SELECT * FROM poke_favourite WHERE name = :name")
    suspend fun getFavouritePokemon(name: String): PokeFavouriteDb

}