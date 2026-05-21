package com.example.aura_style.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main")
    val main: WeatherMain = WeatherMain(),
    @SerializedName("weather")
    val weather: List<WeatherCondition> = emptyList()
) {
    val temperature: Double
        get() = main.temp

    val condition: String
        get() = weather.firstOrNull()?.main.orEmpty()
}

data class WeatherMain(
    @SerializedName("temp")
    val temp: Double = 0.0
)

data class WeatherCondition(
    @SerializedName("main")
    val main: String = ""
)

