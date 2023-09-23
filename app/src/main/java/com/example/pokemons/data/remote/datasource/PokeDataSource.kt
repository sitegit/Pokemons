package com.example.pokemons.data.remote.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokemons.data.remote.api.PokeApiService
import com.example.pokemons.data.remote.model.Result
import com.example.pokemons.util.Constants.PAGE_SIZE
import javax.inject.Inject

class PokeDataSource @Inject constructor(private val api: PokeApiService) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        return try {
            // Получаем номер следующей страницы для загрузки
            val offset = params.key ?: 0

            // Выполняем запрос к API для получения данных
            val response = api.getPokemonsList(offset, PAGE_SIZE)

            // Создаем объект LoadResult.Page с полученными данными и информацией о ключах
            LoadResult.Page(
                data = response.results,  // Список элементов на текущей странице
                prevKey = if (offset > 0) offset - 20 else null,  // Ключ предыдущей страницы
                nextKey = if (offset < response.count) offset + 20 else null  // Ключ следующей страницы
            )
        } catch (e: Exception) {
            // Если возникла ошибка при загрузке данных, возвращаем LoadResult.Error
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        // Логика определения ключа для обновления данных
        Log.i("MyTag", "PokeDataSourg")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}