package com.example.pokemons.domain.usecase

import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.domain.entity.PokeEntryEntity
import javax.inject.Inject

class AddPokemonToFavouriteUseCase @Inject constructor(private val repository: PokemonsRepository) {
    suspend operator fun invoke(favouritePokemon: PokeEntryEntity) {
        repository.addPokemonToFavourite(favouritePokemon)
    }
}