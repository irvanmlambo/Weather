package com.mlamboi.weather.data.remote.api

import com.mlamboi.weather.data.model.WeatherInformation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeather(
        @Query("lat")
        latitude: String,
        @Query("lon")
        longitude: String
    ): Response<WeatherInformation>
}