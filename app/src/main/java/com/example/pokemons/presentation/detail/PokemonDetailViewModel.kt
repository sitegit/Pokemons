package com.example.pokemons.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
import com.example.pokemons.domain.usecase.AddPokemonToFavouriteUseCase
import com.example.pokemons.domain.usecase.DeletePokemonFromFavouriteUseCase
import com.example.pokemons.domain.usecase.GetFavouritePokemonByNameUseCase
import com.example.pokemons.domain.usecase.GetPokemonInfoUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonDetailViewModel @Inject constructor(
    private val getPokemonInfoUseCase: GetPokemonInfoUseCase,
    private val addPokemonToFavouriteUseCase: AddPokemonToFavouriteUseCase,
    private val deletePokemonFromFavouriteUseCase: DeletePokemonFromFavouriteUseCase,
    private val getFavouritePokemonByNameUseCase: GetFavouritePokemonByNameUseCase
): ViewModel() {

    private val _favouritePokemon = MutableLiveData<PokeEntryEntity?>()
    val favouritePokemon: LiveData<PokeEntryEntity?>
        get() = _favouritePokemon

    suspend fun getPokemonInfo(name: String): PokeInfoEntity {
        return getPokemonInfoUseCase.invoke(name)
    }

    fun addPokemonToFavourite(pokeEntryEntity: PokeEntryEntity) {
        viewModelScope.launch {
            addPokemonToFavouriteUseCase.invoke(pokeEntryEntity)
        }
    }

    fun deletePokemonFromFavourite(pokeEntryEntity: PokeEntryEntity) {
        viewModelScope.launch {
            deletePokemonFromFavouriteUseCase.invoke(pokeEntryEntity)
        }
    }

    fun getFavouritePokemonByName(name: String) {
        viewModelScope.launch {
            val pokemon = getFavouritePokemonByNameUseCase.invoke(name)
            _favouritePokemon.value = pokemon
        }
    }

}