package com.example.pokemons.presentation.adapter.rvlist

import androidx.recyclerview.widget.DiffUtil
import com.example.pokemons.data.remote.model.PokeEntry

object PokemonDiffCallBack : DiffUtil.ItemCallback<PokeEntry>() {
    override fun areItemsTheSame(oldItem: PokeEntry, newItem: PokeEntry): Boolean {
        return oldItem.number == newItem.number
    }

    override fun areContentsTheSame(oldItem: PokeEntry, newItem: PokeEntry): Boolean {
        return oldItem == newItem
    }
}