package com.example.pokemons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poke_entries")
data class PokeEntryDbModel(
    @PrimaryKey
    val name: String,
    val number: Int,
    val url: String,
    val page: Int = 0
)
