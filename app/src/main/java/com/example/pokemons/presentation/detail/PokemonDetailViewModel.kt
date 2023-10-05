package com.example.pokemons.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.pokemons.domain.GetPokemonInfoUseCase
import com.example.pokemons.domain.PokeInfoEntity
import javax.inject.Inject

class PokemonDetailViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase
): ViewModel() {

    suspend fun getPokemonInfo(name: String): PokeInfoEntity {
        return getPokemonInfoUseCase.invoke(name)
    }
}