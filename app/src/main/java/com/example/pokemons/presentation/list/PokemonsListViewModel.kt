package com.example.pokemons.presentation.list

import android.nfc.tech.MifareUltralight.PAGE_SIZE
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

    private val _pokemonsListLiveData = MutableLiveData<List<PokemonListEntry>>()
    val pokemonsListLiveData: LiveData<List<PokemonListEntry>> = _pokemonsListLiveData

    init {
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
    }
}

