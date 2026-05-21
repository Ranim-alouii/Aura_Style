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
class OutfitRecommendationViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val preferencesRepository: PreferencesRepository,
    private val wardrobeRepository: WardrobeRepository,
    private val weatherRepository: WeatherRepository,
    private val geminiRepository: com.example.aura_style.data.repository.GeminiRepository
) : ViewModel() {
    private val _weather = MutableStateFlow(WeatherData())
    val weather: StateFlow<WeatherData> = _weather.asStateFlow()

    private val _selectedOccasion = MutableStateFlow("Casual")
    val selectedOccasion: StateFlow<String> = _selectedOccasion.asStateFlow()

    private val _recommendedOutfit = MutableStateFlow(OutfitRecommendation())
    val recommendedOutfit: StateFlow<OutfitRecommendation> = _recommendedOutfit.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _wardrobeItems = MutableStateFlow<List<WardrobeItem>>(emptyList())
    val wardrobeItems: StateFlow<List<WardrobeItem>> = _wardrobeItems.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    companion object {
        private const val TAG = "OutfitRecommendationVM"
    }

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d(TAG, "=== loadInitialData START ===")
                Log.d(TAG, "Loading weather data...")
                loadWeatherData()
                Log.d(TAG, "Weather loaded: ${_weather.value}")
                
                Log.d(TAG, "Loading wardrobe items...")
                _wardrobeItems.value = wardrobeRepository.getAllItems()
                Log.d(TAG, "Wardrobe items loaded: ${_wardrobeItems.value.size} items")
                _wardrobeItems.value.forEach { item ->
                    Log.d(TAG, "  - ${item.name} (${item.category})")
                }
                
                Log.d(TAG, "Generating initial recommendation...")
                generateRecommendationInternal()
                Log.d(TAG, "=== loadInitialData SUCCESS ===")
            } catch (e: Exception) {
                Log.e(TAG, "=== loadInitialData FAILED ===", e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun selectOccasion(occasion: String) {
        _selectedOccasion.value = occasion
        generateRecommendation()
    }

    fun generateRecommendation() {
        Log.d(TAG, ">>> generateRecommendation() called")
        viewModelScope.launch {
            _loading.value = true
            try {
                // Refresh wardrobe items before generating
                Log.d(TAG, "Refreshing wardrobe items...")
                _wardrobeItems.value = wardrobeRepository.getAllItems()
                Log.d(TAG, "Wardrobe items refreshed: ${_wardrobeItems.value.size} items")
                
                Log.d(TAG, "Starting generateRecommendationInternal...")
                generateRecommendationInternal()
                Log.d(TAG, ">>> generateRecommendation() COMPLETED")
            } catch (e: Exception) {
                Log.e(TAG, ">>> generateRecommendation() FAILED", e)
                _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
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
        Log.d(TAG, "=== Generating recommendation for occasion: $occasion ===")

        // Try generating an AI-powered recommendation using Gemini. Fall back to built-in rules on failure.
        try {
            val currentWeather = _weather.value
            val wardrobeItems = _wardrobeItems.value
            Log.d(TAG, "Current weather: temp=${currentWeather.temperature}, condition=${currentWeather.condition}")
            Log.d(TAG, "Wardrobe items count: ${wardrobeItems.size}")

            if (wardrobeItems.isEmpty()) {
                Log.w(TAG, "Warning: No wardrobe items available")
            }

            val prefs = preferencesRepository.getPreferences()
            Log.d(TAG, "Preferences retrieved: style=${prefs?.style}, colors=${prefs?.favoriteColors?.size ?: 0}")

            Log.d(TAG, "Calling geminiRepository.generateOutfitAI...")
            val aiRecommendation = geminiRepository.generateOutfitAI(
                wardrobeItems = wardrobeItems,
                temperature = currentWeather.temperature, // e.g. "20°C"
                condition = currentWeather.condition,
                occasion = occasion,
                stylePreference = prefs?.style,
                favoriteColors = prefs?.favoriteColors ?: emptyList()
            )
            Log.d(TAG, "AI recommendation received: top=${aiRecommendation.top}, bottom=${aiRecommendation.bottom}, shoes=${aiRecommendation.shoes}")

            // Map AI model output (data.model.OutfitRecommendation) to the UI OutfitRecommendation
            val aiItems = aiRecommendation.items.ifEmpty {
                listOf(aiRecommendation.top, aiRecommendation.bottom, aiRecommendation.shoes, aiRecommendation.accessories)
                    .filter { it.isNotBlank() }
            }

            _recommendedOutfit.value = OutfitRecommendation(
                items = aiItems,
                description = aiRecommendation.description
            )
            _errorMessage.value = null
            Log.d(TAG, "✓ Outfit recommendation set successfully with ${aiItems.size} items")
        } catch (e: Exception) {
            Log.e(TAG, "✗ Error generating AI recommendation: ${e.message}", e)
            _errorMessage.value = "Error: ${e.localizedMessage ?: e.message}"
            
            // On failure, fall back to simple rule-based recommendation
            val recommendedItems = when (occasion) {
                "Casual" -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
                "Work" -> listOf("Blazer", "Dress Pants", "Loafers")
                "Sport" -> listOf("Performance Tee", "Athletic Shorts", "Running Shoes")
                "Party" -> listOf("Evening Dress", "Heels", "Clutch")
                else -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
            }

            val description = when (occasion) {
                "Casual" -> "A perfect casual look for your relaxed day out."
                "Work" -> "Professional and polished for your work day."
                "Sport" -> "Stay active and comfortable with this athletic ensemble."
                "Party" -> "Look stunning for your special evening event."
                else -> "A great outfit choice for today."
            }

            _recommendedOutfit.value = OutfitRecommendation(
                items = recommendedItems,
                description = description
            )
            _errorMessage.value = null
        }
    }
}
