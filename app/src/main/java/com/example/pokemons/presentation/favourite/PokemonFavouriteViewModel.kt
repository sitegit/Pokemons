package com.example.pokemons.presentation.favourite

import androidx.lifecycle.ViewModel
import com.example.pokemons.domain.usecase.GetAllFavouritePokemonsUseCase
import javax.inject.Inject

class PokemonFavouriteViewModel @Inject constructor(
    private val getAllFavouritePokemonsUseCase: GetAllFavouritePokemonsUseCase
): ViewModel() {

    val pokemons = getAllFavouritePokemonsUseCase.invoke()
}