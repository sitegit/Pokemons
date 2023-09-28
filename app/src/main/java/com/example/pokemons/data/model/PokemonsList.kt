package com.example.pokemons.data.model

data class PokemonsList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokeEntryDb>
)