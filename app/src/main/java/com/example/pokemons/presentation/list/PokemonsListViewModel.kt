package com.example.pokemons.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.pokemons.data.remote.model.PokeEntry
import com.example.pokemons.domain.GetPokemonsListUseCase
import com.example.pokemons.util.Constants.IMAGE_EXTENSION
import com.example.pokemons.util.Constants.POKEMON_SPRITES_URL
import java.util.Locale
import javax.inject.Inject

class PokemonsListViewModel @Inject constructor(
    private val getPokemonsListUseCase: GetPokemonsListUseCase
) : ViewModel() {

    val pokemons: LiveData<PagingData<PokeEntry>> = getPokemonsListUseCase.invoke()
        .map { pagingData ->
            pagingData.map { result ->
                val number = if (result.url.endsWith("/")) {
                    result.url.dropLast(1).takeLastWhile { it.isDigit() }
                } else {
                    result.url.takeLastWhile { it.isDigit() }
                }
                val url = "$POKEMON_SPRITES_URL$number$IMAGE_EXTENSION"
                PokeEntry(result.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                }, url, number.toInt())
            }
        }
        .cachedIn(viewModelScope)
}




