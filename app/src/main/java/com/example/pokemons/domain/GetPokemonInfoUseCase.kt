package com.example.pokemons.domain

import com.example.pokemons.data.allmodel.Pokemon
import com.example.pokemons.util.Resource
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(private val repository: PokemonsRepository) {

    suspend operator fun invoke(name: String): Resource<Pokemon> =
        repository.getPokemonInfo(name)
}