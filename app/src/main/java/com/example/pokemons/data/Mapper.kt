package com.example.pokemons.data

import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.model.PokeFavouriteDb
import com.example.pokemons.data.model.PokeInfoDb
import com.example.pokemons.data.model.PokeInfoDto
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
import com.example.pokemons.util.Constants
import com.example.pokemons.util.replaceFirstChar
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
        name = pokeEntryDb.name.replaceFirstChar(),
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
        name = (pokeInfoDb.name).replaceFirstChar(),
        number = pokeInfoDb.number,
        weight = pokeInfoDb.weight,
        height = pokeInfoDb.height,
        stats = pokeInfoDb.stats,
        types = pokeInfoDb.types
    )

    fun pokeFavouriteDbToEntryEntity(pokeFavouriteDb: PokeFavouriteDb) = PokeEntryEntity(
        number = pokeFavouriteDb.number,
        name = pokeFavouriteDb.name,
        url = pokeFavouriteDb.imageUrl
    )

    fun listPokeFavouriteDbToPokeEntryEntity(list: List<PokeFavouriteDb>) = list.map {
        pokeFavouriteDbToEntryEntity(it)
    }

    fun pokeEntryEntityToFavouriteDb(pokeEntryEntity: PokeEntryEntity) = PokeFavouriteDb(
        number = pokeEntryEntity.number,
        name = pokeEntryEntity.name,
        imageUrl = pokeEntryEntity.url
    )
}