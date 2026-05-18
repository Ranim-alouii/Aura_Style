package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.repository.PreferencesRepository
import com.example.aura_style.data.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class WeatherData(
    val temperature: String = "72°F",
    val condition: String = "Sunny",
    val date: String = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
)

data class OutfitRecommendation(
    val items: List<String> = listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers"),
    val description: String = "A perfect casual look for your relaxed day out."
)

@HiltViewModel
class OutfitRecommendationViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val wardrobeRepository: WardrobeRepository
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

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                getWeatherData()
                _wardrobeItems.value = wardrobeRepository.getAllItems()
                generateRecommendation()
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
        viewModelScope.launch {
            _loading.value = true
            try {
                val ocasion = _selectedOccasion.value
                val wardrobeitems = _wardrobeItems.value

                val recommendedItems = when (ocasion) {
                    "Casual" -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
                    "Work" -> listOf("Blazer", "Dress Pants", "Loafers")
                    "Sport" -> listOf("Performance Tee", "Athletic Shorts", "Running Shoes")
                    "Party" -> listOf("Evening Dress", "Heels", "Clutch")
                    else -> listOf("Cotton T-Shirt", "Denim Jeans", "Sneakers")
                }

                val description = when (ocasion) {
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
            } finally {
                _loading.value = false
            }
        }
    }

    fun getWeatherData() {
        // Simulate fetching weather data
        // In a real app, this would call a weather API
        _weather.value = WeatherData(
            temperature = "72°F",
            condition = "Sunny",
            date = SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
        )
    }
}

