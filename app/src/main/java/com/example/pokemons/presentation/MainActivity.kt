package com.example.pokemons.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokemons.R
import com.example.pokemons.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBottomMenu()
    }

    private fun setupBottomMenu() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.pokemonDetailFragment -> { binding.bottomNavView.visibility = View.GONE }
                else -> { binding.bottomNavView.visibility = View.VISIBLE }
            }
        }
        binding.bottomNavView.setupWithNavController(navController)
    }
}
