package com.example.pokemons.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokeEntryEntity(
    val number: Int,
    val name: String,
    val url: String
) : Parcelable