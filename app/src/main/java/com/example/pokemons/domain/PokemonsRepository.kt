package com.example.pokemons.domain


import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.pokemons.data.allmodel.Pokemon
import com.example.pokemons.data.remote.model.Result
import com.example.pokemons.util.Resource

interface PokemonsRepository {

    fun getPokePagingSource(): LiveData<PagingData<Result>>

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>
}