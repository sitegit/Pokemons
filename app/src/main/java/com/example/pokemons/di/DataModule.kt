package com.example.pokemons.di

import android.app.Application
import com.example.pokemons.data.PokemonsRepositoryImpl
import com.example.pokemons.data.local.PokeDao
import com.example.pokemons.data.local.PokeDatabase
import com.example.pokemons.data.network.PokeApiFactory
import com.example.pokemons.data.network.PokeApiService
import com.example.pokemons.domain.PokemonsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindPokeAppRepository(impl: PokemonsRepositoryImpl): PokemonsRepository

    companion object {
        @Provides
        @ApplicationScope
        fun providePokeEntryDao(application: Application): PokeDao {
            return PokeDatabase.getInstance(application).pokeEntryDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): PokeApiService {
            return PokeApiFactory.apiService
        }
    }
}