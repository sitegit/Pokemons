package com.example.pokemons.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.pokemons.data.local.PokeEntryDao
import com.example.pokemons.data.model.PokeEntryDbModel
import com.example.pokemons.data.remote.api.PokeApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokeRemoteMediator @Inject constructor(
    private val apiService: PokeApiService,
    private val pokeDao: PokeEntryDao
) : RemoteMediator<Int, PokeEntryDbModel>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokeEntryDbModel>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
                return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val response = apiService.getPokemonsList(offset, limit)

            val pokeEntries = response.getPokeEntryListDbModel()

            if (loadType == LoadType.REFRESH) {
                pokeDao.refresh(pokeEntries)
            }

            pokeDao.insertAll(pokeEntries)

            MediatorResult.Success(
                endOfPaginationReached = pokeEntries.size < limit
            )
        } catch (e: Exception) {
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

