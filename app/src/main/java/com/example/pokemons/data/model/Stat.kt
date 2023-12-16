package com.example.pokemons.data.model

import com.google.gson.annotations.SerializedName

data class Stat(
    @SerializedName("base_stat") val baseStat: Int,
    val stat: StatX
)