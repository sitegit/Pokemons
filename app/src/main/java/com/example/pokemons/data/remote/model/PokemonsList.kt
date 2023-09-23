package com.example.pokemons.data.remote.model

data class PokemonsList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)