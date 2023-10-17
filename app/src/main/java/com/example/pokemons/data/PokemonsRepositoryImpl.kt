package com.example.pokemons.data



import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.model.PokeEntryDb
import com.example.pokemons.data.network.PokeApiService
import com.example.pokemons.domain.PokemonsRepository
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
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
        val pokemonFromDatabase = dao.getPokemon(name.lowercase())
        return mapper.dbInfoToInfoEntity(pokemonFromDatabase)
    }

    override suspend fun addPokemonToFavourite(favouritePokemon: PokeEntryEntity) {
        dao.insertFavouritePokemon(mapper.pokeEntryEntityToFavouriteDb(favouritePokemon))
    }

    override suspend fun deletePokemonFromFavourite(favouritePokemon: PokeEntryEntity) {
        dao.deletePokemon(mapper.pokeEntryEntityToFavouriteDb(favouritePokemon))
    }

    override suspend fun getFavouritePokemonByName(name: String): PokeEntryEntity? {
        val favouritePokemon = dao.getFavouritePokemon(name)
        return if (favouritePokemon != null) {
            mapper.pokeFavouriteDbToEntryEntity(favouritePokemon)
        } else {
            null
        }
    }

    override fun getAllFavouritePokemons(): LiveData<List<PokeEntryEntity>> {
        return MediatorLiveData<List<PokeEntryEntity>>().apply {
            addSource(dao.getAllFavouritePokemons()) {
                value = mapper.listPokeFavouriteDbToPokeEntryEntity(it)
            }
        }
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
