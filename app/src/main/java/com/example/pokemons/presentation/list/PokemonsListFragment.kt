package com.example.pokemons.presentation.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
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
import com.example.pokemons.R
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
        adapterLoadStateListener()
        closeSearchBtnListener()
        observer()
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
                return if (position == pokemonAdapter.itemCount) 2 else 1
            }
        }

        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = pokemonAdapter.withLoadStateFooter(
            footer = PokeLoadStateAdapter { pokemonAdapter.retry() }
        )
    }

    private fun closeSearchBtnListener() {
        val closeBtn = binding.searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeBtn.setOnClickListener {
            binding.searchView.setQuery("", false)
            hideKeyboard()
            isSearching = false
        }
    }

    private fun adapterLoadStateListener() {

        pokemonAdapter.addLoadStateListener { loadState ->
            val isLoadingNow = loadState.refresh is LoadState.Loading

            if (isLoadingNow && isSearching) {
                binding.recyclerView.adapter = pokemonAdapter
                binding.progressBarSpd.visibility = View.VISIBLE
                //Log.i("MyTag", "isLoadingNow && isSearching")
            } else if (isLoadingNow) {
                binding.recyclerView.adapter = pokemonAdapter.withLoadStateFooter(
                    footer = PokeLoadStateAdapter { pokemonAdapter.retry() }
                )
                binding.progressBarSpd.visibility = View.GONE
                //Log.i("MyTag", "isLoadingNow")
            }

            if (loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                binding.progressBarSpd.visibility = View.GONE
                //Log.i("MyTag", "loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached")
            }

            if (loadState.refresh is LoadState.Error) {
                binding.progressBarSpd.visibility = View.GONE
                Toast.makeText(requireContext(), getString(R.string.lost_data), Toast.LENGTH_SHORT).show()
                //Log.e("MyTag", "loadState.refresh is LoadState.Error")
            }

            // Если возникла ошибка при добавлении
            if (loadState.append is LoadState.Error) {
                val error = (loadState.append as LoadState.Error).error
                //Log.e("MyTag", "Error while appending: $error")
                // Можно показать сообщение об ошибке
            }
        }

    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchPokemonList(it) }
                isSearching = true
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchPokemonList(it) }
                isSearching = !newText.isNullOrBlank()
                return true
            }

        })
    }

    private fun hideKeyboard() {
        binding.searchView.clearFocus()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
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