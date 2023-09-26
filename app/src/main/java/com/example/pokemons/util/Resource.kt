package com.example.pokemons.util

import com.example.pokemons.data.model.PokeEntryDbModel


sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    //class Loading<T>(data: T? = null): Resource<T>(data)
}


sealed class State

data class Error(val message: String) : State()
data object Progress : State()
data class Success(val pokemons: List<PokeEntryDbModel>) : State()
