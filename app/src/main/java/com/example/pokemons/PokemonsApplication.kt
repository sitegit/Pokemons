package com.example.pokemons

import android.app.Application
import com.example.pokemons.di.DaggerApplicationComponent

class PokemonsApplication : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }
}