package com.example.pokemons.di

import android.app.Application
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.presentation.list.PokemonsListFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(pokemonsListFragment: PokemonsListFragment)

    fun inject(app: PokemonsApplication)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}