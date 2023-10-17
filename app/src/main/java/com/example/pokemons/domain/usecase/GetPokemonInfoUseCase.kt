package com.example.pokemons.domain.usecase

import com.example.pokemons.domain.entity.PokeInfoEntity
import com.example.pokemons.domain.PokemonsRepository
import javax.inject.Inject

class GetPokemonInfoUseCase @Inject constructor(private val repository: PokemonsRepository) {

    suspend operator fun invoke(name: String): PokeInfoEntity =
        repository.getPokemonInfo(name)
}