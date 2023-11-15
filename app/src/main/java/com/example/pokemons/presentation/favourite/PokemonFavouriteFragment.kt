package com.example.pokemons.presentation.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.databinding.FragmentPokemonFavouriteBinding
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokemonFavouriteAdapter
import javax.inject.Inject

class PokemonFavouriteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PokemonFavouriteViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as PokemonsApplication).component
    }

    private var _binding: FragmentPokemonFavouriteBinding? = null
    private val binding: FragmentPokemonFavouriteBinding
        get() = _binding ?: throw RuntimeException("FragmentPokemonFavouriteBinding == null")

    private val pokemonAdapter = PokemonFavouriteAdapter{ pokemon: PokeEntryEntity, dColor: Int ->
        navigateToDetail(pokemon, dColor)
    }


    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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
        observer()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = pokemonAdapter
    }

    private fun observer() {
        viewModel.pokemons.observe(viewLifecycleOwner) {
            pokemonAdapter.submitList(it)
        }
    }

    private fun navigateToDetail(pokemon: PokeEntryEntity, dominantColor: Int) {
        findNavController().navigate(
            PokemonFavouriteFragmentDirections
                .actionPokemonFavouriteFragmentToPokemonDetailFragment(pokemon, dominantColor)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}