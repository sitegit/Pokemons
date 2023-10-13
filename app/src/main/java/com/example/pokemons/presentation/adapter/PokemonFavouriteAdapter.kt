package com.example.pokemons.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.PokeEntryEntity

class PokemonFavouriteAdapter :
    ListAdapter<PokeEntryEntity, PokemonFavouriteAdapter.PokemonFavouriteViewHolder>(PokemonDiffCallBack) {

    var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonFavouriteViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonFavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonFavouriteViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            //
        }
    }

    inner class PokemonFavouriteViewHolder(
        val binding: PokemonItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    interface OnClickListener {
        fun onClick(pokemon: PokeEntryEntity)
    }
}
