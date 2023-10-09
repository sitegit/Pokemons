package com.example.pokemons.presentation.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
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

    private var isSearching = false
    private var isEndOfPaginationNow = false

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
                query?.let { viewModel.searchPokemonList(it) }
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchPokemonList(it) }

                if (newText.isNullOrBlank()) {
                    binding.progressBarSpd.visibility = View.GONE
                    isSearching = false
                    isEndOfPaginationNow = false
                    updateAdapter()
                }
                return true
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
        updateAdapter()

        val closeBtn = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeBtn.setOnClickListener {
            binding.searchView.setQuery("", false)
            isEndOfPaginationNow = true
            hideKeyboard()
        }
        adapterLoadStateListener()
    }

    private fun updateAdapter() {
        if (isSearching) {
            binding.recyclerView.adapter = pokemonAdapter
        } else {
            binding.recyclerView.adapter = pokemonAdapter.withLoadStateFooter(
                footer = PokeLoadStateAdapter { pokemonAdapter.retry() }
            )
        }
    }

    private fun adapterLoadStateListener() {

        pokemonAdapter.addLoadStateListener { loadState ->
            val isLoadingNow = loadState.refresh is LoadState.Loading
            if (isLoadingNow && loadState.append is LoadState.Loading && !isEndOfPaginationNow) {
                binding.progressBarSpd.visibility = View.VISIBLE
                isSearching = true
                Log.i("MyTag", "isLoadingNow && loadState.append is LoadState.Loading")
                updateAdapter()
            }

            if (loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                Log.i("MyTag", "loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached")
                isEndOfPaginationNow = true
            }

            if (isEndOfPaginationNow) {
                Log.i("MyTag", "isEndOfPaginationNow")
                binding.progressBarSpd.visibility = View.GONE
                isSearching = false
                isEndOfPaginationNow = false
                updateAdapter()
            }
        }
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
