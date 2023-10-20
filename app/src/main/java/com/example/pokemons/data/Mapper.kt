package com.example.pokemons.data

import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.model.PokeFavouriteDb
import com.example.pokemons.data.model.PokeInfoDb
import com.example.pokemons.data.model.PokeInfoDto
import com.example.pokemons.data.model.PokemonsListDto
import com.example.pokemons.data.model.Stat
import com.example.pokemons.data.model.Type
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
import com.example.pokemons.domain.entity.StatEntity
import com.example.pokemons.domain.entity.StatXEntity
import com.example.pokemons.domain.entity.TypeEntity
import com.example.pokemons.domain.entity.TypeXEntity
import com.example.pokemons.util.Constants
import com.example.pokemons.util.replaceFirstChar
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun pokeListToPokeListEntryDb(pokeList: PokemonsListDto): List<PokeEntryDb> {
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

    fun dbEntryToEntryEntity(pokeEntryDb: PokeEntryDb) = PokeEntryEntity(
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
        stats = pokeInfoDb.stats.map { statToStatEntity(it) },
        types = pokeInfoDb.types.map { typeToTypeEntity(it) }
    )

    private fun statToStatEntity(stat: Stat): StatEntity {
        return StatEntity(
            baseStat = stat.baseStat,
            stat = StatXEntity(stat.stat.name)
        )
    }

    private fun typeToTypeEntity(type: Type): TypeEntity {
        return TypeEntity(
            type = TypeXEntity(name = type.type.name)
        )
    }

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