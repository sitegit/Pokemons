package com.example.pokemons.domain


import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PokemonsRepository {

    fun searchPokemonByName(query: String): Flow<PagingData<PokeEntryEntity>>
    fun getPokePagingSource(): Flow<PagingData<PokeEntryEntity>>
    suspend fun getPokemonInfo(name: String): PokeInfoEntity
}