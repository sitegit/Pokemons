package com.example.pokemons.data

import com.example.pokemons.data.model.PokeEntryDbModel
import com.example.pokemons.domain.PokeEntryEntity
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun dBModelToEntity(pokeEntryDbModel: PokeEntryDbModel) = PokeEntryEntity(
        name = pokeEntryDbModel.name,
        url = pokeEntryDbModel.url,
        number = pokeEntryDbModel.number
    )

}