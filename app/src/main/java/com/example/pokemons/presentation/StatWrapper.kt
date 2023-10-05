package com.example.pokemons.presentation

import android.widget.TextView
import com.google.android.material.progressindicator.LinearProgressIndicator

data class StatWrapper(
    val progressBar: LinearProgressIndicator,
    val counter: TextView
)

