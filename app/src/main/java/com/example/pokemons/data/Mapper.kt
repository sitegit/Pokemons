package com.example.pokemons.data

import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.model.PokeInfoDb
import com.example.pokemons.data.model.PokeInfoDto
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.domain.PokeEntryEntity
import com.example.pokemons.domain.PokeInfoEntity
import com.example.pokemons.util.Constants
import java.util.Locale
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun pokeListToPokeListEntryDb(pokeList: PokemonsList): List<PokeEntryDb> {
        return pokeList.results.map { result ->
            val number = if (result.url.endsWith("/")) {
                result.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                result.url.takeLastWhile { it.isDigit() }
            }
            val url = "${Constants.POKEMON_SPRITES_URL}$number${Constants.IMAGE_EXTENSION}"
            val name = result.name

            PokeEntryDb(
                id = number.toInt(),
                name = name,
                url = url
            )
        }
    }

    fun dBEntryToEntryEntity(pokeEntryDb: PokeEntryDb) = PokeEntryEntity(
        name = pokeEntryDb.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        },
        url = pokeEntryDb.url,
        number = pokeEntryDb.id
    )

    fun dtoInfoToInfoDb(pokeInfoDto: PokeInfoDto) = PokeInfoDb(
        name = pokeInfoDto.name,
        number = pokeInfoDto.id,
        weight = pokeInfoDto.weight,
        height = pokeInfoDto.height,
        stats = pokeInfoDto.stats,
        types = pokeInfoDto.types
    )

    fun dbInfoToInfoEntity(pokeInfoDb: PokeInfoDb) = PokeInfoEntity(
        name = pokeInfoDb.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        },
        number = pokeInfoDb.number,
        weight = pokeInfoDb.weight,
        height = pokeInfoDb.height,
        stats = pokeInfoDb.stats,
        types = pokeInfoDb.types
    )


}