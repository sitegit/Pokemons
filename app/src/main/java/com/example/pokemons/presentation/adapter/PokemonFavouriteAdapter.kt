package com.example.pokemons.presentation.adapter

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokemons.R
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.entity.PokeEntryEntity

class PokemonFavouriteAdapter(
    private val navigate: (PokeEntryEntity, Int) -> Unit
) :
    ListAdapter<PokeEntryEntity, PokemonFavouriteAdapter.PokemonFavouriteViewHolder>(PokemonDiffCallBack) {

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
        item?.let {
            holder.bind(item)
        }
    }

    inner class PokemonFavouriteViewHolder(
        private val binding: PokemonItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var dominantColor: Int = 0

        fun bind(pokemon: PokeEntryEntity) {
            binding.apply {
                pokemonTextView.text = pokemon.name
                loadImage(this, pokemon)

                root.setOnClickListener {
                    navigate.invoke(pokemon, dominantColor)
                }
            }
        }

        private fun loadImage(binding: PokemonItemBinding, item: PokeEntryEntity) {
            val imageView = binding.pokemonImageView

            Glide.with(binding.root)
                .asBitmap()
                .load(item.url)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        setColors(resource, binding)
                        return false
                    }
                })
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_none_image)
                .into(imageView)
        }

        private fun setColors(resource: Bitmap, binding: PokemonItemBinding) {
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
}

