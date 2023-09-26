package com.example.pokemons.presentation.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemons.domain.GetPokemonsListUseCase
import com.example.pokemons.domain.PokeEntryEntity
import com.example.pokemons.domain.SearchPokemonUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase
) : ViewModel() {

    // Поток для отслеживания поискового запроса
    private val searchQuery = MutableStateFlow("")

    // Поток данных с учетом поискового запроса
    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemons: Flow<PagingData<PokeEntryEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                getPokemonsListUseCase.invoke()
            } else {
                searchPokemonUseCase.invoke(query)
            }
        }.onEach {
            Log.d("PokemonsListViewModel", "Data emitted: $it")
        }.cachedIn(viewModelScope)

    // Метод для поиска покемонов по запросу
    fun searchPokemonList(query: String) {
        searchQuery.value = query
    }
}




