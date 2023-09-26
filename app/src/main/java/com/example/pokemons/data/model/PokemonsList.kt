package com.example.pokemons.data.model

import com.example.pokemons.util.Constants
import java.util.Locale

data class PokemonsList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<PokeEntryDbModel>
) {
    fun getPokeEntryListDbModel(): List<PokeEntryDbModel> {
        return results.map { result ->
            val number = if (result.url.endsWith("/")) {
                result.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                result.url.takeLastWhile { it.isDigit() }
            }
            val url = "${Constants.POKEMON_SPRITES_URL}$number${Constants.IMAGE_EXTENSION}"
            val name = result.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }

            PokeEntryDbModel(
                number = number.toInt(),
                name = name,
                url = url
            )
        }
    }

}