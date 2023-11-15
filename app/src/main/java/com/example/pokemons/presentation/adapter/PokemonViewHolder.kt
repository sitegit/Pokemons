package com.example.pokemons.presentation.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemons.R
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.util.loadImage

class PokemonViewHolder(
    private val binding: PokemonItemBinding,
    private val navigate: (PokeEntryEntity, Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var dominantColor: Int = 0

    fun bind(pokemon: PokeEntryEntity) {
        binding.apply {
            pokemonTextView.text = pokemon.name
            pokemonImageView.loadImage(pokemon.url) { resource ->
                setColors(resource)
            }
            root.setOnClickListener { navigate.invoke(pokemon, dominantColor) }
        }
    }

    private fun setColors(resource: Bitmap) {
        val palette: Palette = Palette.from(resource).generate()
        val color = palette.dominantSwatch?.rgb ?: R.color.black
        val dominantSwatch = palette.dominantSwatch

        if (dominantSwatch != null) {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(dominantSwatch.rgb, Color.WHITE)
            )

            dominantColor = color
            binding.pokemonCardView.background = gradientDrawable
            binding.pokemonTextView.setTextColor(color)
        }
    }
}