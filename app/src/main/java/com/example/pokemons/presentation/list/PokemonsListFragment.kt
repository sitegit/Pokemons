package com.example.pokemons.presentation.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.databinding.FragmentPokemonsListBinding
import com.example.pokemons.domain.PokeEntryEntity
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokeLoadStateAdapter
import com.example.pokemons.presentation.adapter.PokemonAdapter
import kotlinx.coroutines.launch
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

    private val pokemonAdapter =
        PokemonAdapter { pokemon: PokeEntryEntity, dominantColor: Int ->
            navigateToDetail(pokemon, dominantColor)
        }

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
        initSearchView()
        observer()


    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                // Обрабатывать поисковый запрос, когда пользователь отправляет его
                query?.let {
                    viewModel.searchPokemonList(it)
                }

                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Обрабатывать поисковый запрос по мере его ввода пользователем
                newText?.let {
                    viewModel.searchPokemonList(it)
                }
                return false
            }
        })
    }

    private fun hideKeyboard() {
        binding.searchView.clearFocus()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.pokemons.collect { pagingData ->
                        Log.d("Observer", "Data collected: $pagingData")
                        pokemonAdapter.submitData(pagingData)
                    }
                }
            }
        }
    }


    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Если это элемент состояния загрузки, занимаем 2 колонки
                return if (position == pokemonAdapter.itemCount) 2 else 1
            }
        }

        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = pokemonAdapter.withLoadStateFooter(
            footer = PokeLoadStateAdapter { pokemonAdapter.retry() }
        )

    }

    private fun navigateToDetail(pokemon: PokeEntryEntity, dominantColor: Int) {
        findNavController().navigate(
            PokemonsListFragmentDirections
                .actionPokemonsListFragmentToPokemonDetailFragment(pokemon, dominantColor)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
