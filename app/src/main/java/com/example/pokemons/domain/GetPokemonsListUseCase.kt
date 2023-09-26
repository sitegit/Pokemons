package com.example.pokemons.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonsListUseCase @Inject constructor(private val repository: PokemonsRepository) {
    operator fun invoke(): Flow<PagingData<PokeEntryEntity>> =
        repository.getPokePagingSource()
}