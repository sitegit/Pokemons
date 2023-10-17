package com.example.pokemons.domain


import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
import kotlinx.coroutines.flow.Flow

interface PokemonsRepository {

    fun searchPokemonByName(query: String): Flow<PagingData<PokeEntryEntity>>

    fun getPokemonList(): Flow<PagingData<PokeEntryEntity>>

    suspend fun getPokemonInfo(name: String): PokeInfoEntity

    suspend fun addPokemonToFavourite(favouritePokemon: PokeEntryEntity)

    suspend fun deletePokemonFromFavourite(favouritePokemon: PokeEntryEntity)

    suspend fun getFavouritePokemonByName(name: String): PokeEntryEntity?

    fun getAllFavouritePokemons(): LiveData<List<PokeEntryEntity>>
}