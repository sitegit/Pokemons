package com.example.pokemons.domain

import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(private val repository: PokemonsRepository) {

    suspend operator fun invoke(name: String): PokeInfoEntity =
        repository.getPokemonInfo(name)
}