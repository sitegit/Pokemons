package com.example.pokemons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poke_favourite")
data class PokeFavouriteDb(
    @PrimaryKey
    val name: String,
    val imageUrl: String,
    val number: Int
)
