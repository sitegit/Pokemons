package com.example.pokemons.data.remote.api

import com.example.pokemons.data.allmodel.Pokemon
import com.example.pokemons.data.remote.model.PokemonsList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonsList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonsList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon
}