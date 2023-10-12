package com.example.pokemons.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.network.PokeApiService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokeRemoteMediator @Inject constructor(
    private val apiService: PokeApiService,
    private val pokeDao: PokeDao,
    private val mapper: Mapper,
    private val query: String? = null
) : RemoteMediator<Int, PokeEntryDb>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokeEntryDb>
    ): MediatorResult {

        pageIndex =
            getPageIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val response = apiService.getPokemonsList(offset, limit)
            val pokeEntries = mapper.pokeListToPokeListEntryDb(response)

            if (loadType == LoadType.REFRESH) { pokeDao.refresh(pokeEntries) }

            val cleanedQuery = query?.replace("%", "") ?: ""
            val relevantPokeEntries = if (query.isNullOrBlank()) pokeEntries
            else pokeEntries.filter { it.name.contains(cleanedQuery, ignoreCase = true) }

            supervisorScope {
                val detailedInfoJobs = relevantPokeEntries.map { poke ->
                    async {
                        val pokemon = apiService.getPokemonInfo(poke.name.lowercase())
                        mapper.dtoInfoToInfoDb(pokemon)
                    }
                }
                val detailedInfos = detailedInfoJobs.awaitAll()
                pokeDao.insertAllInfo(detailedInfos)
                pokeDao.insertAll(pokeEntries)
            }

            MediatorResult.Success(endOfPaginationReached = pokeEntries.size < limit)
        } catch (e: Exception) {
            Log.e("PokeRemoteMediator", "Error occurred: ${e.message}", e)
            MediatorResult.Error(e)
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

