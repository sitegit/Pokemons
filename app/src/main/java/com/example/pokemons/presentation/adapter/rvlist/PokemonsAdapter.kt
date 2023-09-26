package com.example.pokemons.presentation.adapter.rvlist

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokemons.R
import com.example.pokemons.databinding.PokemonItemBinding
import com.example.pokemons.domain.PokeEntryEntity

class PokemonsAdapter : PagingDataAdapter<PokeEntryEntity, PokemonViewHolder>(PokemonDiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = PokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            loadImage(holder, it)
            holder.binding.pokemonTextView.text = it.name
        }
    }

    private fun loadImage(holder: PokemonViewHolder, item: PokeEntryEntity) {
        val imageView = holder.binding.pokemonImageView

        Glide.with(holder.itemView.context)
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
                    setColors(resource, holder)
                    return false
                }
            })
            .placeholder(R.mipmap.ic_launcher)
            .error(R.drawable.baseline_mood_bad_24)
            .into(imageView)
    }

    private fun setColors(resource: Bitmap, holder: PokemonViewHolder) {
        val palette: Palette = Palette.from(resource).generate()
        val color = palette.dominantSwatch?.rgb ?: R.color.black
        val dominantSwatch = palette.dominantSwatch

        if (dominantSwatch != null) {
            val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(dominantSwatch.rgb, Color.WHITE)
            )
            holder.binding.pokemonCardView.background = gradientDrawable
            holder.binding.pokemonTextView.setTextColor(color)
        }
    }
}


