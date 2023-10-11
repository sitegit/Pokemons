package com.example.pokemons.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.util.ConnectivityObserver
import com.example.pokemons.util.NetworkConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private val component by lazy {
        (this.application as PokemonsApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                val length = when (status) {
                    ConnectivityObserver.Status.Available -> Snackbar.LENGTH_SHORT
                    else -> Snackbar.LENGTH_INDEFINITE
                }

                showSnackbar(status.toString(), length)
            }
        }

    }

    private fun showSnackbar(message: String, length: Int) {
        val contextView = findViewById<View>(R.id.main_container)
        val snackbar = Snackbar.make(contextView, "Network status: $message", length)
        val textView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

        val color = if (length == Snackbar.LENGTH_SHORT) R.color.green else R.color.red
        textView.setTextColor(ContextCompat.getColor(this, color))
        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
        snackbar.show()
    }
}
