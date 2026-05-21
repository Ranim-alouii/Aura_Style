package com.example.aura_style.data.repository

import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.remote.GeminiService
import com.example.aura_style.data.remote.dto.*
import com.google.gson.Gson
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
class GeminiRepositoryTest {

    @Mock
    private lateinit var geminiService: GeminiService

    private val gson = Gson()
    private lateinit var repository: GeminiRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = GeminiRepository(geminiService, gson)
    }

    @Test
    fun generateOutfitAI_returnsParsedRecommendationOnSuccess() = runTest {
        val wardrobeItems = listOf(
            WardrobeItem(name = "Silk Shirt", category = "Tops", color = "White", season = "Summer")
        )
        val jsonResponse = """
            {
              "top": "Silk Shirt",
              "bottom": "Chinos",
              "shoes": "Loafers",
              "accessories": "Watch",
              "description": "Smart casual look"
            }
        """.trimIndent()

        val mockResponse = GeminiGenerateContentResponse(
            candidates = listOf(
                GeminiCandidate(
                    content = GeminiResponseContent(
                        parts = listOf(GeminiPart(text = jsonResponse))
                    )
                )
            )
        )

        whenever(geminiService.generateOutfitAI(any(), any())).thenReturn(mockResponse)

        val result = repository.generateOutfitAI(wardrobeItems, "22°C", "Sunny", "Work")

        assertNotNull(result)
        assertEquals("Silk Shirt", result.top)
        assertEquals("Smart casual look", result.description)
    }

    @Test
    fun generateOutfitAI_returnsFallbackOnApiError() = runTest {
        val wardrobeItems = listOf(
            WardrobeItem(name = "Basic Tee", category = "Tops", color = "White", season = "All")
        )
        whenever(geminiService.generateOutfitAI(any(), any())).thenThrow(RuntimeException("Network Error"))

        val result = repository.generateOutfitAI(wardrobeItems, "20°C", "Cloudy", "Casual")

        assertNotNull(result)
        assertEquals("Basic Tee", result.top)
    }
}
