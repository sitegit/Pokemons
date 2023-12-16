package com.example.pokemons.domain.usecase

import androidx.paging.PagingData
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.PokemonsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPokemonUseCase @Inject constructor(private val repository: PokemonsRepository) {

    operator fun invoke(query: String): Flow<PagingData<PokeEntryEntity>> =
        repository.searchPokemonByName(query)
}