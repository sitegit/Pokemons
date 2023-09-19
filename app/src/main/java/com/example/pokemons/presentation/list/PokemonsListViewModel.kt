package com.example.pokemons.presentation.list

import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemons.data.PokemonListEntry
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.domain.GetPokemonsListUseCase
import com.example.pokemons.util.Error
import com.example.pokemons.util.Factorial
import com.example.pokemons.util.Progress
import com.example.pokemons.util.Resource
import com.example.pokemons.util.State
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase
) : ViewModel() {


    private var curPage = 0

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() {
        //_state.value = Progress
        viewModelScope.launch {
            val result = getPokemonsListUseCase.invoke(PAGE_SIZE, curPage * PAGE_SIZE)
            when(result) {
                is Resource.Success -> {
                    //_endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data?.results?.mapIndexed { index, entry ->
                        val number = if(entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokemonListEntry(entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, url, number.toInt())
                    }
                    curPage++

                     pokedexEntries?.let {
                        _state.value = Factorial(it)
                    }
                }
                is Resource.Error -> {
                    _state.value = Error(result.message!!)
                }
            }
        }
    }

}

