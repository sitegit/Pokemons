package com.example.pokemons.domain

import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.util.Resource
import javax.inject.Inject

class GetPokemonsListUseCase @Inject constructor(private val repository: PokemonsRepository) {

    suspend operator fun invoke(offset: Int, limit: Int): Resource<PokemonsList> =
        repository.getPokemonList(offset, limit)
}