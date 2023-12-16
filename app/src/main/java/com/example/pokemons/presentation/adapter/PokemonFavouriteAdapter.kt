package com.example.pokemons.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.entity.PokeEntryEntity

class PokemonFavouriteAdapter(
    private val navigate: (PokeEntryEntity, Int) -> Unit
) : ListAdapter<PokeEntryEntity, PokemonViewHolder>(PokemonDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            navigate
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }
}

