package com.example.pokemons.data.model

data class PokemonsListDto(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokeEntryDto>
)