package com.example.pokemons.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.network.PokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokeRemoteMediator @Inject constructor(
    private val apiService: PokeApiService,
    private val pokeDao: PokeDao,
    private val mapper: Mapper,
    private val query: String? = null
) : RemoteMediator<Int, PokeEntryDb>() {

    private var pageIndex = 0
    private val tempList = mutableListOf<PokeEntryDb>()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokeEntryDb>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit


        return withContext(Dispatchers.IO) {
            try {

                val response = apiService.getPokemonsList(offset, limit)

                val pokeEntries = mapper.pokeListToPokeListEntryDb(response)

                if (loadType == LoadType.REFRESH) { pokeDao.refresh(pokeEntries) }

                if (query.isNullOrBlank()) {
                    val detailedInfoJobs = pokeEntries.map { poke ->
                        async {
                            val pokemon = apiService.getPokemonInfo(poke.name.lowercase())
                            mapper.dtoInfoToInfoDb(pokemon)
                        }
                    }
                    val detailedInfos = detailedInfoJobs.awaitAll()
                    pokeDao.insertAllInfo(detailedInfos)
                }

                pokeDao.insertAll(pokeEntries)

                MediatorResult.Success(
                    endOfPaginationReached = pokeEntries.size < limit
                )
            } catch (e: Exception) {
                MediatorResult.Error(e)
            }
        }

    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

}

