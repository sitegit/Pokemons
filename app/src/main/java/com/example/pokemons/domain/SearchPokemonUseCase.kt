package com.example.pokemons.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(private val repository: PokemonsRepository) {

    operator fun invoke(query: String): Flow<PagingData<PokeEntryEntity>> =
        repository.searchPokemonByName(query)
}