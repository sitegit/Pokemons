package com.example.pokemons.di.module

import android.app.Application
import com.example.pokemons.di.ApplicationScope
import com.example.pokemons.util.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides

@Module
interface NetworkModule {

    companion object {
        @Provides
        @ApplicationScope
        fun provideNetworkConnectivityObserver(application: Application): NetworkConnectivityObserver {
            return NetworkConnectivityObserver(application)
        }
    }
}