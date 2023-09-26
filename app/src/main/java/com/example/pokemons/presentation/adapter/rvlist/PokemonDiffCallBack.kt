package com.example.pokemons.presentation.adapter.rvlist

import androidx.recyclerview.widget.DiffUtil
import com.example.pokemons.domain.PokeEntryEntity

object PokemonDiffCallBack : DiffUtil.ItemCallback<PokeEntryEntity>() {
    override fun areItemsTheSame(oldItem: PokeEntryEntity, newItem: PokeEntryEntity): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PokeEntryEntity, newItem: PokeEntryEntity): Boolean {
        return oldItem == newItem
    }
}