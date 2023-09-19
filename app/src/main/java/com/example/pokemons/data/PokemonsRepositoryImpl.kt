package com.example.pokemons.data


import com.example.pokemons.data.model.Pokemon
import com.example.pokemons.data.model.PokemonsList
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.util.Resource
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PokemonsRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonsRepository {
    override suspend fun getPokemonList(offset: Int, limit: Int): Resource<PokemonsList> {
        val response = try {
            apiService.getPokemonsList(offset, limit)
        } catch (e: Exception) {
            val errorMessage = getError(e)
            return Resource.Error(errorMessage)
        }
        return Resource.Success(response)
    }

    override suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        val response = try {
            apiService.getPokemonInfo(name)
        } catch (e: Exception) {
            val errorMessage = getError(e)
            return Resource.Error(errorMessage)
        }
        return Resource.Success(response)
    }

    private fun getError(e: Exception): String {
        return when (e) {
            is IOException -> "Network error. Please check your internet connection."
            is HttpException -> "HTTP error: ${e.code()}"
            else -> "An unknown error occurred."
        }
    }
}