package com.example.pokemons.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemons.databinding.ItemLoadingStateBinding

class PokeLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<PokeLoadStateAdapter.PokeLoadStateViewHolder>() {

    inner class PokeLoadStateViewHolder(
        private val binding: ItemLoadingStateBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        // Привязка состояния загрузки к элементам интерфейса
        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textViewError.text = loadState.error.localizedMessage
            }

            binding.progressbar.visible(loadState is LoadState.Loading)
            binding.buttonRetry.visible(loadState is LoadState.Error)
            binding.textViewError.visible(loadState is LoadState.Error)

            // Обработчик кнопки "Retry"
            binding.buttonRetry.setOnClickListener {
                retry()
            }
        }

        // Вспомогательная функция для управления видимостью
        private fun View.visible(isVisible: Boolean) {
            visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }

    // Привязка состояния загрузки к ViewHolder
    override fun onBindViewHolder(holder: PokeLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    // Создание ViewHolder для состояния загрузки
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = PokeLoadStateViewHolder(
        ItemLoadingStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}
