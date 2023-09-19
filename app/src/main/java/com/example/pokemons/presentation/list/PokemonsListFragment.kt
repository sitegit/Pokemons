package com.example.pokemons.presentation.list

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
import com.example.pokemons.R
import com.example.pokemons.databinding.FragmentPokemonsListBinding
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokemonsAdapter
import javax.inject.Inject

class PokemonsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PokemonsListViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as PokemonsApplication).component
    }

    private var _binding: FragmentPokemonsListBinding? = null
    private val binding: FragmentPokemonsListBinding
        get() = _binding ?: throw RuntimeException("FragmentPokemonsListBinding == null")

    private val pokemonsAdapter = PokemonsAdapter()

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.pokemonsList.observe(viewLifecycleOwner) {
            viewModel.loadPokemonPaginated()
            pokemonsAdapter.submitList(it)
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = pokemonsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}