package com.example.pokemons.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.pokemons.data.remote.model.Result
import javax.inject.Inject

class GetPokemonsListUseCase @Inject constructor(private val repository: PokemonsRepository) {

    operator fun invoke(): LiveData<PagingData<Result>> =
        repository.getPokePagingSource()
}