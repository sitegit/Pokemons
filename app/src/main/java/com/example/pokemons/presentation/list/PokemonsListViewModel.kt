package com.example.pokemons.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.usecase.GetPokemonsListUseCase
import com.example.pokemons.domain.usecase.SearchPokemonUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

// Ваш ViewModel
class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    var appBarVerticalOffset = 0

    //private val _networkStatus = MutableSharedFlow<ConnectivityObserver.Status>()
    //val networkStatus: SharedFlow<ConnectivityObserver.Status> get() = _networkStatus

    @OptIn(ExperimentalCoroutinesApi::class)
    val pokemons: Flow<PagingData<PokeEntryEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                getPokemonsListUseCase.invoke()
            } else {
                searchPokemonUseCase.invoke(query)
            }
        }.cachedIn(viewModelScope)

    fun searchPokemonList(query: String) {
        searchQuery.value = query
    }

    /*private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                _networkStatus.emit(status)
            }
        }
    }*/
}




