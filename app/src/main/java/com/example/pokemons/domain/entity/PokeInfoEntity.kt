package com.example.pokemons.domain.entity

import com.example.pokemons.data.model.Stat
import com.example.pokemons.data.model.Type

data class PokeInfoEntity(
    val name: String,
    val number: Int,
    val weight: Int,
    val height: Int,
    val stats: List<Stat>,
    val types: List<Type>
)
