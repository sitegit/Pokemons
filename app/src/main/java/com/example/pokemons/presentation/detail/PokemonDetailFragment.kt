package com.example.pokemons.presentation.detail

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokemons.PokemonsApplication
import com.example.pokemons.R
import com.example.pokemons.databinding.FragmentPokemonDetailBinding
import com.example.pokemons.databinding.PokemonStatsBinding
import com.example.pokemons.domain.PokeInfoEntity
import com.example.pokemons.presentation.ViewModelFactory
import com.example.pokemons.util.Constants.COUNTER_OFFSET
import com.example.pokemons.util.Constants.COUNTER_SMALL_PROGRESS_OFFSET
import com.example.pokemons.util.Constants.COUNTER_THRESHOLD
import com.example.pokemons.util.replaceFirstChar
import com.google.android.material.progressindicator.LinearProgressIndicator
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

    private var _statsBinding: PokemonStatsBinding? = null
    private val statsBinding: PokemonStatsBinding
        get() = _statsBinding ?: throw RuntimeException("PokemonStatsBinding == null")

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
        _statsBinding = PokemonStatsBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayTextInfo()
        setDominantColors(args.dominantColor)
        Glide.with(view).load(args.pokemon.url).into(binding.ivPokemonImg)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun displayTextInfo() {
        viewModel.viewModelScope.launch(Dispatchers.Main) {
            with(binding) {
                val pokemon = viewModel.getPokemonInfo(args.pokemon.name)
                val drawable = getDrawableTv(args.dominantColor)
                tvPokemonNumber.text = getString(R.string.prefix, args.pokemon.number.toString())
                tvPokemonName.text = pokemon.name
                tvHeight.text = pokemon.height.toString()
                tvWeight.text = pokemon.weight.toString()

                if (pokemon.types.size == 1) {
                    tvTypeCenter.text = pokemon.types[0].type.name.replaceFirstChar()
                    tvTypeCenter.visibility = View.VISIBLE
                    tvTypeCenter.background = drawable
                } else {
                    tvTypeLeft.text = pokemon.types[0].type.name.replaceFirstChar()
                    tvTypeRight.text = pokemon.types[1].type.name.replaceFirstChar()
                    tvTypeLeft.visibility = View.VISIBLE
                    tvTypeRight.visibility = View.VISIBLE
                    tvTypeLeft.background = drawable
                    tvTypeRight.background = drawable
                }

                getStat(pokemon)
            }

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

        animateProgress(0, specs, progressBar, counter)
    }

    private fun animateProgress(start: Int, end: Int, progressBar: LinearProgressIndicator, counter: TextView) {
        ValueAnimator.ofInt(start, end).apply {
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

    private fun setCounterPosition(progress: Int, progressBar: LinearProgressIndicator, counter: TextView) {
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
        val typedValue = TypedValue()
        activity?.theme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        activity?.window?.statusBarColor = typedValue.data
    }
    override fun onDestroyView() {
        super.onDestroyView()
        setDefaultColorStatusBar()
        _binding = null
        _statsBinding = null
    }
}