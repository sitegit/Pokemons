package com.example.pokemons.data.model

data class PokeInfoDto(
    val name: String,
    val id: Int,
    val weight: Int,
    val height: Int,
    val stats: List<Stat>,
    val types: List<Type>
)