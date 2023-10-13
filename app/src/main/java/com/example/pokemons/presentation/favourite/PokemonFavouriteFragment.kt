package com.example.pokemons.presentation.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemons.databinding.FragmentPokemonFavouriteBinding
import com.example.pokemons.presentation.adapter.PokemonFavouriteAdapter

class PokemonFavouriteFragment : Fragment() {
    private var _binding: FragmentPokemonFavouriteBinding? = null
    private val binding: FragmentPokemonFavouriteBinding
        get() = _binding ?: throw RuntimeException("FragmentPokemonFavouriteBinding == null")

    private val pokemonAdapter = PokemonFavouriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = pokemonAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}