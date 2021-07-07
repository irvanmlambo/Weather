package com.mlamboi.weather.data.remote.repository

import com.mlamboi.weather.data.remote.api.RetrofitClient

class WeatherRepository {
    suspend fun getWeatherInformation(
        latitude: String,
        longitude: String
    ) = RetrofitClient.api.getWeather(latitude, longitude)
}