package com.example.pokemons.di

import android.app.Application
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.presentation.MainActivity
import com.example.pokemons.presentation.detail.PokemonDetailFragment
import com.example.pokemons.presentation.list.PokemonsListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class, NetworkModule::class])
interface ApplicationComponent {

    fun inject(pokemonsListFragment: PokemonsListFragment)

    fun inject(pokemonDetailFragment: PokemonDetailFragment)

    fun inject(mainActivity: MainActivity)

    fun inject(app: PokemonsApplication)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}