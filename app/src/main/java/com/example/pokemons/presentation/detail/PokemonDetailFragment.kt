package com.example.pokemons.presentation.detail

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.databinding.FragmentPokemonDetailBinding
import com.example.pokemons.presentation.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PokemonDetailFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[PokemonDetailViewModel::class.java]
    }

    private val component by lazy {
        (requireActivity().application as PokemonsApplication).component
    }

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding: FragmentPokemonDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentPokemonDetailBinding == null")

    private val args by navArgs<PokemonDetailFragmentArgs>()

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDominantColors(args.dominantColor)

        viewModel.viewModelScope.launch(Dispatchers.Main) {
            with(binding) {
                val pokemon = viewModel.getPokemonInfo(args.pokemon.name)
                Glide.with(view).load(args.pokemon.url).into(ivPokemonImg)
                tvPokemonNumber.text = getString(R.string.prefix, args.pokemon.number.toString())
                tvPokemonName.text = pokemon.name
                tvHeight.text = pokemon.height.toString()
                tvWeight.text = pokemon.weight.toString()

                if (pokemon.types.size == 1) {
                    tvTypeCenter.text = pokemon.types[0].type.name
                }
                else {
                    tvTypeLeft.text = pokemon.types[0].type.name
                    tvTypeRight.text = pokemon.types[1].type.name
                }

                tvHp.text = pokemon.stats[0].baseStat.toString()
                tvAttack.text = pokemon.stats[1].baseStat.toString()
                tvDefense.text = pokemon.stats[2].baseStat.toString()
                tvSpecialAttack.text = pokemon.stats[3].baseStat.toString()
                tvSpecialDefense.text = pokemon.stats[4].baseStat.toString()
                tvSpeed.text = pokemon.stats[5].baseStat.toString()
            }

        }

    }

    private fun setDominantColors(dominantColor: Int) {
        val window = activity?.window
        window?.statusBarColor = dominantColor
        binding.layoutDetail.setBackgroundColor(dominantColor)
    }

    private fun setDefaultColorStatusBar() {
        val typedValue = TypedValue()
        activity?.theme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        activity?.window?.statusBarColor = typedValue.data
    }
    override fun onDestroyView() {
        super.onDestroyView()
        setDefaultColorStatusBar()
        _binding = null
    }
}