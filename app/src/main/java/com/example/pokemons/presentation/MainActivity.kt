package com.example.pokemons.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.util.NetworkConnectivityObserver
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
                showToast("Network status: $status")
            }
        }

    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
