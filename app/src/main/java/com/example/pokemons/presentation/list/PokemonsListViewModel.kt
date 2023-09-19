package com.example.pokemons.presentation.list

import com.example.pokemons.util.Constants.PAGE_SIZE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemons.data.PokemonListEntry
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.domain.GetPokemonsListUseCase
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.util.Resource
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase
) : ViewModel() {

    //private val _pokemonsListLiveData = MutableLiveData<List<PokemonListEntry>>()
    //val pokemonsListLiveData: LiveData<List<PokemonListEntry>> = _pokemonsListLiveData

    private var curPage = 0

    private val _pokemonList = MutableLiveData<List<PokemonListEntry>>()
    val pokemonsList: LiveData<List<PokemonListEntry>> = _pokemonList

    private val _loadError = MutableLiveData<String>("")
    val loadError: LiveData<String> = _loadError

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _endReached = MutableLiveData<Boolean>(false)
    val endReached: LiveData<Boolean> = _isLoading


    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getPokemonsListUseCase.invoke(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    _endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokemonListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                    }
                    curPage++

                    _loadError.value = ""
                    _isLoading.value = false
                    _pokemonList.value = pokedexEntries
                }
                is Resource.Error -> {
                    _loadError.value = result.message!!
                    _isLoading.value = false
                }
            }
        }
    }

    /*init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = getPokemonsListUseCase.invoke(20, 20)

            val pokemonEntries = result.data?.results?.mapIndexed { index, entry ->
                val number = if (entry.url.endsWith("/")) {
                    entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                } else {
                    entry.url.takeLastWhile { it.isDigit() }
                }
                val url =
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                PokemonListEntry(entry.name.capitalize(Locale.ROOT), url, number.toInt())
            }

            _pokemonsListLiveData.value = pokemonEntries!! // Обновите LiveData с результатами
        }
    }*/
}

