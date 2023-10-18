package com.example.pokemons.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.databinding.ActivityMainBinding
import com.example.pokemons.util.ConnectivityObserver
import com.example.pokemons.util.NetworkConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    private val component by lazy {
        (this.application as PokemonsApplication).component
    }

    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observer()
        setupBottomMenu()
    }

    private fun setupBottomMenu() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pokemonDetailFragment -> {
                    binding.bottomNavView.visibility = View.GONE
                    snackBar?.dismiss()
                }
                else -> {
                    binding.bottomNavView.visibility = View.VISIBLE
                    snackBar?.show()
                }
            }
        }
        binding.bottomNavView.setupWithNavController(navController)
    }


    private fun observer() {

        lifecycleScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                val length = when (status) {
                    ConnectivityObserver.Status.Available -> Snackbar.LENGTH_SHORT
                    else -> Snackbar.LENGTH_INDEFINITE
                }

                showSnackbar(status.toString(), length)
                if (status == ConnectivityObserver.Status.Available) snackBar = null
            }
        }
    }

    private fun showSnackbar(status: String, length: Int) {
        val color = if (length == Snackbar.LENGTH_SHORT) R.color.green else R.color.red
        snackBar = Snackbar.make(binding.mainContainer, "Network status: $status", length)
            .setTextColor(ContextCompat.getColor(this, color))
            .setAnchorView(binding.bottomNavView)

        snackBar?.let {
            it.animationMode = Snackbar.ANIMATION_MODE_SLIDE
            it.show()
        }

    }
}
