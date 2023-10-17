package com.example.pokemons.presentation.detail

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.databinding.FragmentPokemonDetailBinding
import com.example.pokemons.databinding.PokemonStatsBinding
import com.example.pokemons.domain.entity.PokeEntryEntity
import com.example.pokemons.domain.entity.PokeInfoEntity
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.util.Constants.COUNTER_OFFSET
import com.example.pokemons.util.Constants.COUNTER_SMALL_PROGRESS_OFFSET
import com.example.pokemons.util.Constants.COUNTER_THRESHOLD
import com.example.pokemons.util.replaceFirstChar
import com.google.android.material.progressindicator.LinearProgressIndicator
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

    private var _statsBinding: PokemonStatsBinding? = null
    private val statsBinding: PokemonStatsBinding
        get() = _statsBinding ?: throw RuntimeException("PokemonStatsBinding == null")

    private val args by navArgs<PokemonDetailFragmentArgs>()
    private lateinit var pokemon: PokeInfoEntity
    private var pokeFavourite: PokeEntryEntity? = null

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        _statsBinding = PokemonStatsBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPokemon()
        setDominantColors(args.dominantColor)
        Glide.with(view).load(args.pokemon.url).into(binding.ivPokemonImg)
        onClickChangeFavourite()
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setPokemon() {
        lifecycleScope.launch {
            pokemon = viewModel.getPokemonInfo(args.pokemon.name)
            displayTextInfo(pokemon)
        }
    }

    private fun onClickChangeFavourite() {
        viewModel.getFavouritePokemonByName(args.pokemon.name)

        viewModel.favouritePokemon.observe(viewLifecycleOwner) {
            pokeFavourite = it
            updateBookmark(pokeFavourite != null)
        }
        favouritePokemonListener()
    }

    private fun favouritePokemonListener() {
        binding.bookmark.setOnClickListener {

            val isFavourite = pokeFavourite == null
            val pokemon = PokeEntryEntity(args.pokemon.number, args.pokemon.name, args.pokemon.url)
            pokeFavourite = if (isFavourite) {
                viewModel.addPokemonToFavourite(pokemon)
                pokemon
            } else {
                viewModel.deletePokemonFromFavourite(pokemon)
                null
            }

            updateBookmark(isFavourite)
            showToast(isFavourite)
        }
    }


    private fun showToast(isChecked: Boolean) {
        val message = if (isChecked)
            getString(R.string.added_to_favorites, args.pokemon.name)
        else
            getString(R.string.removed_from_favorites, args.pokemon.name)

        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateBookmark(isChecked: Boolean) {
        val color = if (isChecked) R.color.yellow else R.color.grey

        binding.bookmark.backgroundTintList =
            ContextCompat.getColorStateList(requireContext(), color)
    }

    private fun displayTextInfo(poke: PokeInfoEntity) {
        with(binding) {
            val drawable = getDrawableTv(args.dominantColor)
            tvPokemonNumber.text = getString(R.string.prefix, args.pokemon.number.toString())
            tvPokemonName.text = poke.name
            tvHeight.text = poke.height.toString()
            tvWeight.text = poke.weight.toString()

            if (poke.types.size == 1) {
                tvTypeCenter.text = poke.types[0].type.name.replaceFirstChar()
                tvTypeCenter.visibility = View.VISIBLE
                tvTypeCenter.background = drawable
            } else {
                tvTypeLeft.text = poke.types[0].type.name.replaceFirstChar()
                tvTypeRight.text = poke.types[1].type.name.replaceFirstChar()
                tvTypeLeft.visibility = View.VISIBLE
                tvTypeRight.visibility = View.VISIBLE
                tvTypeLeft.background = drawable
                tvTypeRight.background = drawable
            }
            getStat(poke)
        }

    }

    private fun getDrawableTv(color: Int): Drawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = 50f
        drawable.setColor(color)
        return drawable
    }

    private fun getStat(pokemon: PokeInfoEntity) {
        val maxStat = pokemon.stats.maxBy { it.baseStat }.baseStat

        val wrappers = listOf(
            StatWrapper(statsBinding.progressBarHp, statsBinding.tvCounterHp),
            StatWrapper(statsBinding.progressBarAtk, statsBinding.tvCounterAtk),
            StatWrapper(statsBinding.progressBarDef, statsBinding.tvCounterDef),
            StatWrapper(statsBinding.progressBarSpAtk, statsBinding.tvCounterSpAtk),
            StatWrapper(statsBinding.progressBarSpDef, statsBinding.tvCounterSpDef),
            StatWrapper(statsBinding.progressBarSpd, statsBinding.tvCounterSpd)
        )

        wrappers.forEachIndexed { index, wrapper ->
            if (index < pokemon.stats.size) {
                wrapper.progressBar.max = maxStat
                setProgress(wrapper.progressBar, wrapper.counter, pokemon.stats[index].baseStat)
            }
        }
    }

    private fun setProgress(progressBar: LinearProgressIndicator, counter: TextView, specs: Int) {
        setCounterPosition(0, progressBar, counter)

        statsBinding.progressBarSpd.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            setCounterPosition(progressBar.progress, progressBar, counter)
        }

        animateProgress(specs, progressBar, counter)
    }

    private fun animateProgress(
        specs: Int,
        progressBar: LinearProgressIndicator,
        counter: TextView
    ) {
        ValueAnimator.ofInt(0, specs).apply {
            duration = 700
            interpolator = AccelerateInterpolator()
            addUpdateListener { animation ->
                val progressValue = animation.animatedValue as Int
                progressBar.progress = progressValue
                counter.text = progressValue.toString()

                setCounterPosition(progressValue, progressBar, counter)
            }
            start()
        }
    }

    private fun setCounterPosition(
        progress: Int,
        progressBar: LinearProgressIndicator,
        counter: TextView
    ) {
        val progressRatio = progress.toFloat() / progressBar.max
        val progressBarWidth = progressBar.width - progressBar.paddingStart - progressBar.paddingEnd
        counter.visibility = View.VISIBLE

        if (progressBarWidth * progressRatio > COUNTER_THRESHOLD) {
            if (progressBar.progress < COUNTER_SMALL_PROGRESS_OFFSET) {
                counter.x = progressBarWidth * progressRatio - counter.width
            } else {
                counter.x = progressBarWidth * progressRatio - COUNTER_OFFSET
            }
        } else {
            counter.visibility = View.GONE
        }
    }

    private fun setDominantColors(dominantColor: Int) {
        val window = activity?.window
        window?.statusBarColor = dominantColor
        binding.layoutDetail.setBackgroundColor(dominantColor)
    }

    private fun setDefaultColorStatusBar() {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.red)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        setDefaultColorStatusBar()
        _binding = null
        _statsBinding = null
    }
}