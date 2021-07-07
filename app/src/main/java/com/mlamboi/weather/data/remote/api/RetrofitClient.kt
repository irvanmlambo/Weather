package com.mlamboi.weather.data.remote.api

import com.mlamboi.weather.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private val retrofit by lazy {
            val httpClient = OkHttpClient.Builder().addInterceptor(QueryParameterInterceptor()).apply {

            }.build()

            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build()
        }

        val api by lazy {
            retrofit.create(WeatherService::class.java)
        }
    }
}