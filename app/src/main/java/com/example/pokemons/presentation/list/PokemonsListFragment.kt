package com.example.pokemons.presentation.list

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.databinding.FragmentPokemonsListBinding
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokeLoadStateAdapter
import com.example.pokemons.presentation.adapter.rvlist.PokemonsAdapter
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
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Обрабатывать поисковый запрос по мере его ввода пользователем
                newText?.let {
                    viewModel.searchPokemonList(it)
                }
                return true
            }


        })
    }

    private fun observer() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.pokemons.collect { pagingData ->
                        Log.d("Observer", "Data collected: $pagingData")
                        pokemonsAdapter.submitData(pagingData)
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
                return if (position == pokemonsAdapter.itemCount) 2 else 1
            }
        }

        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.adapter = pokemonsAdapter.withLoadStateFooter(
            footer = PokeLoadStateAdapter { pokemonsAdapter.retry() }
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Слушатель состояния загрузки
/*// Добавляем слушатель на состояния загрузки
                launch {
                    pokemonsAdapter.loadStateFlow.collectLatest { loadStates ->
                        // Логируем состояние обновления
                        when (loadStates.refresh) {
                            is LoadState.Loading -> Log.d("LoadState", "Обновление: Загрузка")
                            is LoadState.NotLoading -> Log.d("LoadState", "Обновление: Загрузка завершена")
                            is LoadState.Error -> Log.d("LoadState", "Обновление: Ошибка ${(loadStates.refresh as LoadState.Error).error.message}")
                        }

                        // Логируем состояние предзагрузки
                        when (loadStates.prepend) {
                            is LoadState.Loading -> Log.d("LoadState", "Предзагрузка: Загрузка")
                            is LoadState.NotLoading -> Log.d("LoadState", "Предзагрузка: Загрузка завершена")
                            is LoadState.Error -> Log.d("LoadState", "Предзагрузка: Ошибка ${(loadStates.prepend as LoadState.Error).error.message}")
                        }

                        // Логируем состояние добавления
                        when (loadStates.append) {
                            is LoadState.Loading -> Log.d("LoadState", "Добавление: Загрузка")
                            is LoadState.NotLoading -> {
                                Log.d("LoadState", "Добавление: Загрузка завершена")
                                if (loadStates.append.endOfPaginationReached) {
                                    Log.d("LoadState", "Добавление: Конец пагинации достигнут")
                                }
                            }
                            is LoadState.Error -> Log.d("LoadState", "Добавление: Ошибка ${(loadStates.append as LoadState.Error).error.message}")
                        }

                        if (loadStates.refresh is LoadState.NotLoading && loadStates.append.endOfPaginationReached) {
                            // Загрузка завершена
                            Log.d("LoadState", "Общее состояние: Загрузка завершена и конец пагинации достигнут")

                        }
                    }
                }*/

