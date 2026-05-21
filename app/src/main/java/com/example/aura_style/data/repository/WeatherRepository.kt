package com.example.aura_style.data.repository

import com.example.aura_style.data.remote.WeatherService
import com.example.aura_style.data.remote.dto.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherService: WeatherService
) {
    suspend fun fetchWeather(lat: Double, lon: Double): WeatherResponse {
        return weatherService.getCurrentWeather(lat, lon)
    }
}

