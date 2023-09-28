package com.example.pokemons.data



import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.mediator.PokeRemoteMediator
import com.example.pokemons.data.network.api.PokeApiService
import com.example.pokemons.domain.PokeEntryEntity
import com.example.pokemons.domain.PokeInfoEntity
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.util.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class PokemonsRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val dao: PokeDao,
    private val mapper: Mapper
) : PokemonsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokePagingSource(): Flow<PagingData<PokeEntryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            remoteMediator = PokeRemoteMediator(apiService, dao, mapper)
        ) {
            dao.getPokemons()
        }.flow.map { pagingData ->
            pagingData.map { mapper.dBEntryToEntryEntity(it) }
        }
    }

    override fun searchPokemonByName(query: String): Flow<PagingData<PokeEntryEntity>> {
        // добавьте символы % вокруг запроса для поиска, чтобы использовать SQL оператор LIKE
        val adjustedQuery = "%${query.trim()}%"
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            //remoteMediator = PokeRemoteMediator(apiService, dao)
        ) {
            dao.searchPokemons(adjustedQuery)
        }.flow.map { pagingData ->
            pagingData.map { mapper.dBEntryToEntryEntity(it) }
        }
    }


    override suspend fun getPokemonInfo(name: String): PokeInfoEntity {

        return mapper.dbInfoToInfoEntity(dao.getPokemon(name.lowercase()))
        //return mapper.dBModelToInfoEntity(dao.getPokemon(name))
        /*val response = try {
            apiService.getPokemonInfo(name)
        } catch (e: Exception) {
            val errorMessage = getError(e)
            return Resource.Error(errorMessage)
        }
        return Resource.Success(response)*/
    }

    private fun getError(e: Exception): String {
        return when (e) {
            is IOException -> "Network error. Please check your internet connection."
            is HttpException -> "HTTP error: ${e.code()}"
            else -> "An unknown error occurred."
        }
    }
}