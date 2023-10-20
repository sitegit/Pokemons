package com.example.pokemons.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "poke_entries")
data class PokeEntryDb(
    @PrimaryKey
    val name: String,
    val id: Int,
    val url: String
)
