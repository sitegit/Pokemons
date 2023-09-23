package com.example.pokemons.data.remote



import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.pokemons.data.allmodel.Pokemon
import com.example.pokemons.data.remote.api.PokeApiService
import com.example.pokemons.data.remote.datasource.PokeDataSource
import com.example.pokemons.data.remote.model.Result
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.util.Constants.PAGE_SIZE
import com.example.pokemons.util.Resource
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PokemonsRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
) : PokemonsRepository {

    override fun getPokePagingSource(): LiveData<PagingData<Result>> {

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                PokeDataSource(apiService)
            }
        ).liveData
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