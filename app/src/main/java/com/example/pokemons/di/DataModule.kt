package com.example.pokemons.di

import android.app.Application
import com.example.pokemons.data.PokemonsRepositoryImpl
import com.example.pokemons.data.local.PokeDatabase
import com.example.pokemons.data.local.PokeEntryDao
import com.example.pokemons.data.remote.api.PokeApiFactory
import com.example.pokemons.data.remote.api.PokeApiService
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
        fun providePokeEntryDao(application: Application): PokeEntryDao {
            return PokeDatabase.getInstance(application).pokeEntryDao()
        }

        /*@Provides
        @ApplicationScope
        fun providePokeDatabase(application: Application): PokeDatabase {
            return PokeDatabase.getInstance(application)
        }*/

        @Provides
        @ApplicationScope
        fun provideApiService(): PokeApiService {
            return PokeApiFactory.apiService
        }
    }
}