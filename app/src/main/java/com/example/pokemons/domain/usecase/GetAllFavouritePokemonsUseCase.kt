package com.example.pokemons.domain.usecase

import androidx.lifecycle.LiveData
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.domain.entity.PokeEntryEntity
import javax.inject.Inject

class GetAllFavouritePokemonsUseCase @Inject constructor(private val repository: PokemonsRepository) {

    operator fun invoke(): LiveData<List<PokeEntryEntity>> =
        repository.getAllFavouritePokemons()
}