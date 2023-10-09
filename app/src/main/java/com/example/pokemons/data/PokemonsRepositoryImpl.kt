package com.example.pokemons.data



import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.network.PokeApiService
import com.example.pokemons.domain.PokeEntryEntity
import com.example.pokemons.domain.PokeInfoEntity
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.util.Constants.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonsRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService,
    private val dao: PokeDao,
    private val mapper: Mapper
) : PokemonsRepository {

    override fun getPokemonList(): Flow<PagingData<PokeEntryEntity>> {
        return getPagedFlow { dao.getPokemons() }
    }

    override fun searchPokemonByName(query: String): Flow<PagingData<PokeEntryEntity>> {
        val adjustedQuery = "%${query.trim()}%"
        return getPagedFlow(query = adjustedQuery) { dao.searchPokemons(adjustedQuery) }
    }

    override suspend fun getPokemonInfo(name: String): PokeInfoEntity {
        return mapper.dbInfoToInfoEntity(dao.getPokemon(name.lowercase()))
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getPagedFlow(
        query: String? = null,
        daoFunction: () -> PagingSource<Int, PokeEntryDb>
    ): Flow<PagingData<PokeEntryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = true,
            ),
            remoteMediator = PokeRemoteMediator(apiService, dao, mapper, query)
        ) {
            daoFunction.invoke()
        }.flow.map { pagingData ->
            pagingData.map { mapper.dBEntryToEntryEntity(it) }
        }
    }
}
