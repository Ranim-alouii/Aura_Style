package com.example.aura_style.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.remote.dto.WeatherResponse
import com.example.aura_style.data.repository.PreferencesRepository
import com.example.aura_style.data.repository.WardrobeRepository
import com.example.aura_style.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.util.Log
import kotlin.math.roundToInt

data class WeatherData(
    val temperature: String = "72°F",
    val condition: String = "Sunny",
    val date: String = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
)

data class OutfitRecommendation(
    val items: List<String> = listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers"),
    val description: String = "A perfect casual look for your relaxed day out."
)

private const val DEFAULT_LATITUDE = 40.7128
private const val DEFAULT_LONGITUDE = -74.0060

private fun WeatherResponse.toWeatherData(): WeatherData = WeatherData(
    temperature = "${temperature.roundToInt()}°C",
    condition = condition.ifBlank { "Clear" },
    date = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
)

@HiltViewModel
open class OutfitRecommendationViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val wardrobeRepository: WardrobeRepository,
    private val weatherRepository: WeatherRepository,
    private val geminiRepository: com.example.aura_style.data.repository.GeminiRepository
) : ViewModel() {
    protected val _weather = MutableStateFlow(WeatherData())
    open val weather: StateFlow<WeatherData> = _weather.asStateFlow()

    protected val _selectedOccasion = MutableStateFlow("Casual")
    open val selectedOccasion: StateFlow<String> = _selectedOccasion.asStateFlow()

    protected val _recommendedOutfit = MutableStateFlow(OutfitRecommendation())
    open val recommendedOutfit: StateFlow<OutfitRecommendation> = _recommendedOutfit.asStateFlow()

    protected val _loading = MutableStateFlow(false)
    open val loading: StateFlow<Boolean> = _loading.asStateFlow()

    protected val _wardrobeItems = MutableStateFlow<List<WardrobeItem>>(emptyList())
    open val wardrobeItems: StateFlow<List<WardrobeItem>> = _wardrobeItems.asStateFlow()

    protected val _errorMessage = MutableStateFlow<String?>(null)
    open val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    companion object {
        private const val TAG = "OutfitRecommendationVM"
    }

    init {
        loadInitialData()
    }

    protected open fun loadInitialData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                loadWeatherData()
                _wardrobeItems.value = wardrobeRepository.getAllItems()
                generateRecommendationInternal()
            } catch (e: Exception) {
                Log.e(TAG, "loadInitialData FAILED", e)
            } finally {
                _loading.value = false
            }
        }
    }

    open fun selectOccasion(occasion: String) {
        _selectedOccasion.value = occasion
        generateRecommendation()
    }

    open fun generateRecommendation() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _wardrobeItems.value = wardrobeRepository.getAllItems()
                generateRecommendationInternal()
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun getWeatherData() {
        viewModelScope.launch {
            loadWeatherData()
        }
    }

    private suspend fun loadWeatherData() {
        try {
            val (lat, lon) = getCurrentCoordinates() ?: DEFAULT_LATITUDE to DEFAULT_LONGITUDE
            val response = weatherRepository.fetchWeather(lat, lon)
            _weather.value = response.toWeatherData()
        } catch (_: Exception) {
            _weather.value = WeatherData()
        }
    }

    private suspend fun getCurrentCoordinates(): Pair<Double, Double>? = suspendCoroutine { continuation ->
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            continuation.resume(null)
            return@suspendCoroutine
        }

        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener { location ->
                continuation.resume(location?.let { it.latitude to it.longitude })
            }
            .addOnFailureListener {
                continuation.resume(null)
            }
    }

    private suspend fun generateRecommendationInternal() {
        val occasion = _selectedOccasion.value
        try {
            val currentWeather = _weather.value
            val wardrobeItems = _wardrobeItems.value
            val prefs = preferencesRepository.getPreferences()

            val aiRecommendation = geminiRepository.generateOutfitAI(
                wardrobeItems = wardrobeItems,
                temperature = currentWeather.temperature,
                condition = currentWeather.condition,
                occasion = occasion,
                stylePreference = prefs?.style,
                favoriteColors = prefs?.favoriteColors ?: emptyList()
            )

            val aiItems = aiRecommendation.items.ifEmpty {
                listOf(aiRecommendation.top, aiRecommendation.bottom, aiRecommendation.shoes, aiRecommendation.accessories)
                    .filter { it.isNotBlank() }
            }

            _recommendedOutfit.value = OutfitRecommendation(
                items = aiItems,
                description = aiRecommendation.description
            )
            _errorMessage.value = null
        } catch (e: Exception) {
            val recommendedItems = when (occasion) {
                "Casual" -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
                "Work" -> listOf("Blazer", "Dress Pants", "Loafers")
                "Sport" -> listOf("Performance Tee", "Athletic Shorts", "Running Shoes")
                "Party" -> listOf("Evening Dress", "Heels", "Clutch")
                else -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
            }
            _recommendedOutfit.value = OutfitRecommendation(
                items = recommendedItems,
                description = "A standard outfit choice."
            )
        }
    }
}
