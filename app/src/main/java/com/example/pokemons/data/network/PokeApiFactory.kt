package com.example.pokemons.data.network

import com.example.pokemons.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PokeApiFactory {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getInterceptor())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    val apiService: PokeApiService = retrofit.create(PokeApiService::class.java)
}