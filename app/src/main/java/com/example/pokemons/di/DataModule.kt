package com.example.pokemons.di

import com.example.pokemons.data.PokeApiFactory
import com.example.pokemons.data.PokeApiService
import com.example.pokemons.data.PokemonsRepositoryImpl
import com.example.pokemons.domain.PokemonsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCryptoAppRepository(impl: PokemonsRepositoryImpl): PokemonsRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideApiService(): PokeApiService {
            return PokeApiFactory.apiService
        }
    }
}