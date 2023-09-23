package com.example.pokemons.di

import com.example.pokemons.data.remote.api.PokeApiFactory
import com.example.pokemons.data.remote.api.PokeApiService
import com.example.pokemons.data.remote.PokemonsRepositoryImpl
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
        fun provideApiService(): PokeApiService {
            return PokeApiFactory.apiService
        }
    }
}