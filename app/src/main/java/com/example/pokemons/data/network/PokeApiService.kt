package com.example.pokemons.data.network

import com.example.pokemons.data.model.PokeInfoDto
import com.example.pokemons.data.model.PokemonsListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonsList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PokemonsListDto

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokeInfoDto
}