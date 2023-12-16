package com.example.pokemons.util

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokemons.R
import java.util.Locale

fun String.replaceFirstChar(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}

fun ImageView.loadImage(url: String, onResourceReady: (Bitmap) -> Unit) {
    Glide.with(this)
        .asBitmap()
        .load(url)
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
                onResourceReady(resource)
                return false
            }
        })
        .placeholder(R.mipmap.ic_launcher)
        .error(R.drawable.ic_none_image)
        .into(this)
}
