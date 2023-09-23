package com.example.pokemons.presentation.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.databinding.FragmentPokemonsListBinding
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.presentation.adapter.PokeLoadStateAdapter
import com.example.pokemons.presentation.adapter.rvlist.PokemonsAdapter
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
        observer()

    }

    private fun observer() {
       viewModel.pokemons.observe(viewLifecycleOwner) {
            pokemonsAdapter.submitData(lifecycle, it)
       }
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Если это элемент состояния загрузки (можете добавить дополнительные условия по вашему усмотрению), занимаем 2 колонки
                return if (position == pokemonsAdapter.itemCount) 2 else 1
            }
        }

        binding.recyclerView.layoutManager = layoutManager

        // Применение адаптера к списку с поддержкой состояния загрузки
        binding.recyclerView.adapter = pokemonsAdapter.withLoadStateHeaderAndFooter(
            header = PokeLoadStateAdapter { pokemonsAdapter.retry() },
            footer = PokeLoadStateAdapter { pokemonsAdapter.retry() }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Слушатель состояния загрузки
/*pokemonsAdapter.addLoadStateListener { loadState ->
    when (loadState.refresh) {
        is LoadState.Loading -> {
            // Показать индикатор загрузки
            //binding.progressbar.visibility = View.VISIBLE
            //binding.errorLayout.visibility = View.GONE
        }
        is LoadState.Error -> {
            // Скрыть индикатор загрузки и показать ошибку
            //binding.progressbar.visibility = View.GONE
            //binding.errorLayout.visibility = View.VISIBLE
        }
        is LoadState.NotLoading -> {
            // Скрыть индикатор загрузки
            //binding.progressbar.visibility = View.GONE
            // Проверка, если есть данные
            if (pokemonsAdapter.itemCount > 0) {
                //binding.errorLayout.visibility = View.GONE
            } else {
                // Показать сообщение об ошибке, если данных нет
                //binding.errorLayout.visibility = View.VISIBLE
            }
        }
    }
}*/