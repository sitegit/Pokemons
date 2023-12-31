package com.example.pokemons.presentation.list

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokeLoadStateAdapter
import com.example.pokemons.presentation.adapter.PokemonAdapter
import com.example.pokemons.util.ConnectivityObserver
import com.example.pokemons.util.NetworkConnectivityObserver
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.abs

class PokemonsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

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
    private var isUnknownHostException = false

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
        appBarListener()
        setDefaultColorStatusBar()
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

                launch {
                    networkConnectivityObserver.observe().collect { status ->
                        val length = when (status) {
                            ConnectivityObserver.Status.Available -> {
                                if (isUnknownHostException) {
                                    pokemonAdapter.retry()
                                    isUnknownHostException = false
                                }
                                Snackbar.LENGTH_SHORT
                            }
                            else -> Snackbar.LENGTH_INDEFINITE
                        }

                        showSnackBar(status.toString(), length)
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

    private fun appBarListener() {
        val maxRadius = resources.getDimension(R.dimen.max_corner_radius)
        var currentRadius = maxRadius
        val isExpanded = viewModel.appBarVerticalOffset == 0

        setAppBarCornerRadius(binding.appBar, maxRadius)
        binding.appBar.setExpanded(isExpanded, false)

        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
            viewModel.appBarVerticalOffset = verticalOffset
            val desiredRadius = (1 - seekPosition) * maxRadius

            if (currentRadius != desiredRadius) {
                appbarAnimateCornerRadius(appBarLayout, currentRadius, desiredRadius)
                currentRadius = desiredRadius
            }
        }
    }

    private fun setAppBarCornerRadius(appBarLayout: AppBarLayout, radius: Float) {
        val background = appBarLayout.background as MaterialShapeDrawable
        background.shapeAppearanceModel = background.shapeAppearanceModel
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .build()

    }

    private fun appbarAnimateCornerRadius(appBarLayout: AppBarLayout, fromRadius: Float, toRadius: Float) {
        val animator = ValueAnimator.ofFloat(fromRadius, toRadius)
        animator.addUpdateListener { valueAnimator ->
            val isClosed = abs(viewModel.appBarVerticalOffset) == binding.appBar.totalScrollRange
            val radius = if (isClosed) 0f else valueAnimator.animatedValue as Float
            setAppBarCornerRadius(appBarLayout, radius)
        }
        animator.start()
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
            } else if (isLoadingNow) {
                binding.recyclerView.adapter = pokemonAdapter.withLoadStateFooter(
                    footer = PokeLoadStateAdapter { pokemonAdapter.retry() }
                )
                binding.progressBarSpd.visibility = View.GONE
            }

            if (loadState.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                binding.progressBarSpd.visibility = View.GONE
            }

            if (loadState.refresh is LoadState.Error) {
                val error = (loadState.refresh as LoadState.Error).error
                if (error is UnknownHostException) {
                    isUnknownHostException = true
                }
                binding.progressBarSpd.visibility = View.GONE
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

    private fun showSnackBar(status: String, length: Int) {
        val color = if (length == Snackbar.LENGTH_SHORT) R.color.green else R.color.red
        val snackBar = Snackbar.make(binding.recyclerView, "Network status: $status", length)
            .setTextColor(ContextCompat.getColor(requireContext(), color))

        snackBar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
        snackBar.show()
    }

    private fun setDefaultColorStatusBar() {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.red)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}