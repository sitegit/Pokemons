package com.example.pokemons.domain


import androidx.paging.PagingData
import com.example.pokemons.data.allmodel.Pokemon
import com.example.pokemons.util.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonsRepository {

    fun searchPokemonByName(query: String): Flow<PagingData<PokeEntryEntity>>
    fun getPokePagingSource(): Flow<PagingData<PokeEntryEntity>>

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>
}