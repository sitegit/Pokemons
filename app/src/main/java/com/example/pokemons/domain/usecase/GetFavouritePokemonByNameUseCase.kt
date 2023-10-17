package com.example.pokemons.domain.usecase

import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.domain.entity.PokeEntryEntity
import javax.inject.Inject

class GetFavouritePokemonByNameUseCase @Inject constructor(private val repository: PokemonsRepository) {
    suspend operator fun invoke(name: String): PokeEntryEntity? =
        repository.getFavouritePokemonByName(name)
}