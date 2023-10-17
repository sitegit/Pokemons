package com.example.pokemons.domain.usecase

import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.PokemonsRepository
import javax.inject.Inject

class DeletePokemonFromFavouriteUseCase @Inject constructor(private val repository: PokemonsRepository) {
    suspend operator fun invoke(favouritePokemon: PokeEntryEntity) {
        repository.deletePokemonFromFavourite(favouritePokemon)
    }
}