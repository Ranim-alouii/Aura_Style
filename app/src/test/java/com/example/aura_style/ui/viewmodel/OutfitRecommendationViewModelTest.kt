package com.example.aura_style.ui.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aura_style.data.local.entity.Preferences
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.model.OutfitRecommendation as AIOutfitRecommendation
import com.example.aura_style.data.remote.dto.WeatherCondition
import com.example.aura_style.data.remote.dto.WeatherMain
import com.example.aura_style.data.remote.dto.WeatherResponse
import com.example.aura_style.data.repository.GeminiRepository
import com.example.aura_style.data.repository.PreferencesRepository
import com.example.aura_style.data.repository.WardrobeRepository
import com.example.aura_style.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.clearInvocations
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class OutfitRecommendationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock private lateinit var context: Context
    @Mock private lateinit var wardrobeRepository: WardrobeRepository
    @Mock private lateinit var weatherRepository: WeatherRepository
    @Mock private lateinit var preferencesRepository: PreferencesRepository
    @Mock private lateinit var geminiRepository: GeminiRepository

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OutfitRecommendationViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(context.checkSelfPermission(any())).thenReturn(PackageManager.PERMISSION_DENIED)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setupMocks(
        wardrobeItems: List<WardrobeItem> = listOf(
            WardrobeItem(name = "Silk Shirt", category = "Top", color = "White", season = "Summer"),
            WardrobeItem(name = "Chinos", category = "Bottom", color = "Beige", season = "All"),
            WardrobeItem(name = "Loafers", category = "Shoes", color = "Brown", season = "All")
        ),
        prefs: Preferences? = Preferences(style = "Classic", favoriteColors = listOf("Blue")),
        weather: WeatherResponse = WeatherResponse(
            main = WeatherMain(temp = 20.0),
            weather = listOf(WeatherCondition(main = "Clear"))
        ),
        recommendation: AIOutfitRecommendation = AIOutfitRecommendation(
            top = "Silk Shirt",
            bottom = "Chinos",
            shoes = "Loafers",
            description = "Smart casual look"
        )
    ) = runBlocking {
        whenever(wardrobeRepository.getAllItems()).thenReturn(wardrobeItems)
        whenever(weatherRepository.fetchWeather(any(), any())).thenReturn(weather)
        whenever(preferencesRepository.getPreferences()).thenReturn(prefs)
        whenever(
            geminiRepository.generateOutfitAI(
                wardrobeItems = any(),
                temperature = any(),
                condition = any(),
                occasion = any(),
                stylePreference = any(),
                favoriteColors = any(),
                apiKey = any()
            )
        ).thenReturn(recommendation)
    }

    private fun initViewModel() {
        viewModel = OutfitRecommendationViewModel(
            context = context,
            preferencesRepository = preferencesRepository,
            wardrobeRepository = wardrobeRepository,
            weatherRepository = weatherRepository,
            geminiRepository = geminiRepository
        )
    }

    @Test
    fun `selectOccasion updates state and triggers generation`() = runTest {
        setupMocks()
        initViewModel()
        advanceUntilIdle()
        clearInvocations(geminiRepository)

        viewModel.selectOccasion("Party")
        advanceUntilIdle()

        assertEquals("Party", viewModel.selectedOccasion.value)
        assertNotNull(viewModel.recommendedOutfit.value)
        assertEquals("Smart casual look", viewModel.recommendedOutfit.value.description)
    }

    @Test
    fun `generateRecommendation with empty wardrobe uses fallback`() = runTest {
        setupMocks(wardrobeItems = emptyList(), prefs = null)
        initViewModel()
        advanceUntilIdle()

        viewModel.generateRecommendation()
        advanceUntilIdle()

        assertNotNull(viewModel.recommendedOutfit.value)
        assertFalse(viewModel.recommendedOutfit.value.items.isEmpty())
    }
}
