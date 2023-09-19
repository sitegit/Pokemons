package com.example.pokemons.domain

import androidx.lifecycle.LiveData
import com.example.pokemons.data.model.Pokemon
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.util.Resource

interface PokemonsRepository {

    suspend fun getPokemonList(offset: Int, limit: Int): Resource<PokemonsList>

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>
}