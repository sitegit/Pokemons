package com.example.pokemons.util

import java.util.Locale

fun String.replaceFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}