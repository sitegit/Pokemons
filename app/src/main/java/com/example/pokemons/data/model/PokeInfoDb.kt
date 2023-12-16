package com.example.pokemons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poke_info")
data class PokeInfoDb(
    @PrimaryKey
    val name: String,
    val number: Int,
    val weight: Int,
    val height: Int,
    val stats: List<Stat>,
    val types: List<Type>
)
