package com.example.pokemons.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.PokeEntryEntity

class PokemonAdapter(
    private val navigate: (PokeEntryEntity, Int) -> Unit
) : PagingDataAdapter<PokeEntryEntity, PokemonViewHolder>(PokemonDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding, navigate)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(item)
        }
    }
}


