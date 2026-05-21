package com.example.aura_style.data.repository

import com.example.aura_style.data.remote.WeatherService
import com.example.aura_style.data.remote.dto.WeatherCondition
import com.example.aura_style.data.remote.dto.WeatherMain
import com.example.aura_style.data.remote.dto.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherRepositoryTest {

    @Mock
    private lateinit var weatherService: WeatherService

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = WeatherRepository(weatherService)
    }

    @Test
    fun fetchWeather_returnsParsedWeatherResponse() = runTest {
        val mockResponse = WeatherResponse(
            main = WeatherMain(temp = 22.5),
            weather = listOf(WeatherCondition(main = "Rain"))
        )
        whenever(weatherService.getCurrentWeather(any(), any(), any(), any())).thenReturn(mockResponse)

        val result = repository.fetchWeather(40.7128, -74.0060)

        assertNotNull(result)
        assertEquals(22.5, result.temperature, 0.0)
        assertEquals("Rain", result.condition)
    }

    @Test
    fun fetchWeather_handlesDifferentTemperatureValues() = runTest {
        val mockResponse = WeatherResponse(
            main = WeatherMain(temp = -5.0),
            weather = listOf(WeatherCondition(main = "Snow"))
        )
        whenever(weatherService.getCurrentWeather(any(), any(), any(), any())).thenReturn(mockResponse)

        val result = repository.fetchWeather(0.0, 0.0)

        assertEquals(-5.0, result.temperature, 0.0)
        assertEquals("Snow", result.condition)
    }
}

