package com.example.pokemons.di

import androidx.lifecycle.ViewModel
import com.example.pokemons.presentation.detail.PokemonDetailViewModel
import com.example.pokemons.presentation.list.PokemonsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PokemonsListViewModel::class)
    fun bindPokeListViewModel(viewModel: PokemonsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PokemonDetailViewModel::class)
    fun bindPokeDetailViewModel(viewModel: PokemonDetailViewModel): ViewModel
}