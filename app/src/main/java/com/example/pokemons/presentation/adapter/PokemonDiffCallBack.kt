package com.example.pokemons.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.pokemons.data.PokemonListEntry
import com.example.pokemons.data.model.Pokemon

object PokemonDiffCallBack : DiffUtil.ItemCallback<PokemonListEntry>() {
    override fun areItemsTheSame(oldItem: PokemonListEntry, newItem: PokemonListEntry): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: PokemonListEntry, newItem: PokemonListEntry): Boolean {
        return oldItem == newItem
    }
}