package com.example.aura_style.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.aura_style.ui.viewmodel.OutfitRecommendation
import com.example.aura_style.ui.viewmodel.OutfitRecommendationViewModel
import com.example.aura_style.ui.viewmodel.WeatherData
import com.example.aura_style.ui.viewmodel.FavoritesViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

/**
 * Fake implementation of OutfitRecommendationViewModel to bypass Mockito's 
 * SecurityException on Android 14+.
 */
class FakeOutfitViewModel : OutfitRecommendationViewModel(
    mock(android.content.Context::class.java),
    mock(com.example.aura_style.data.repository.PreferencesRepository::class.java),
    mock(com.example.aura_style.data.repository.WardrobeRepository::class.java),
    mock(com.example.aura_style.data.repository.WeatherRepository::class.java),
    mock(com.example.aura_style.data.repository.GeminiRepository::class.java)
) {
    override val weather = MutableStateFlow(WeatherData(temperature = "25°C", condition = "Sunny"))
    override val selectedOccasion = MutableStateFlow("Casual")
    override val recommendedOutfit = MutableStateFlow(OutfitRecommendation(description = "Perfect summer outfit"))
    override val loading = MutableStateFlow(false)
    override val errorMessage = MutableStateFlow<String?>(null)
}

/**
 * Fake implementation of FavoritesViewModel.
 */
class FakeFavoritesViewModel : FavoritesViewModel(
    mock(com.example.aura_style.data.repository.FavoriteRepository::class.java)
) {
    override val favorites = MutableStateFlow(emptyList<com.example.aura_style.data.local.entity.Favorite>())
}

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeOutfitViewModel = FakeOutfitViewModel()
    private val fakeFavoritesViewModel = FakeFavoritesViewModel()

    @Test
    fun homeScreen_rendersGreetingAndWeather() {
        composeTestRule.setContent {
            HomeScreen(
                outfitRecommendationViewModel = fakeOutfitViewModel,
                favoritesViewModel = fakeFavoritesViewModel
            )
        }

        // Verify key UI elements are displayed
        composeTestRule.onNodeWithText("Good morning, Fashionista").assertIsDisplayed()
        composeTestRule.onNodeWithText("25°C").assertIsDisplayed()
        composeTestRule.onNodeWithText("Sunny").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysRecommendation() {
        composeTestRule.setContent {
            HomeScreen(
                outfitRecommendationViewModel = fakeOutfitViewModel,
                favoritesViewModel = fakeFavoritesViewModel
            )
        }

        // Verify the recommendation card content
        composeTestRule.onNodeWithText("AI Recommendation").assertIsDisplayed()
        composeTestRule.onNodeWithText("Perfect summer outfit").assertIsDisplayed()
    }

    @Test
    fun homeScreen_containsGenerateButton() {
        composeTestRule.setContent {
            HomeScreen(
                outfitRecommendationViewModel = fakeOutfitViewModel,
                favoritesViewModel = fakeFavoritesViewModel
            )
        }

        // Verify the action button exists and is clickable
        composeTestRule.onNodeWithText("Generate New").assertIsDisplayed()
        composeTestRule.onNodeWithText("Generate New").assertHasClickAction()
    }
}
