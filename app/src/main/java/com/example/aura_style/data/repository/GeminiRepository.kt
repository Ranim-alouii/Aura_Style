package com.example.aura_style.data.repository

import com.example.aura_style.BuildConfig
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.model.OutfitRecommendation
import com.example.aura_style.data.remote.GeminiService
import com.example.aura_style.data.remote.dto.GeminiContent
import com.example.aura_style.data.remote.dto.GeminiGenerateContentRequest
import com.example.aura_style.data.remote.dto.GeminiPart
import com.example.aura_style.data.remote.dto.GeminiGenerateContentResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class GeminiRepository @Inject constructor(
    private val geminiService: GeminiService,
    private val gson: Gson
) {
    companion object {
        private const val TAG = "GeminiRepository"
    }

    suspend fun generateOutfitAI(
        wardrobeItems: List<WardrobeItem>,
        temperature: String,
        condition: String,
        occasion: String,
        stylePreference: String? = null,
        favoriteColors: List<String> = emptyList(),
        apiKey: String = BuildConfig.GEMINI_API_KEY
    ): OutfitRecommendation {
        Log.d(TAG, ">>> generateOutfitAI called")
        Log.d(TAG, "API Key present: ${apiKey.isNotBlank()}")
        Log.d(TAG, "API Key length: ${apiKey.length}")
        if (apiKey.isBlank()) {
            Log.e(TAG, "ERROR: API key is blank! Cannot call Gemini API")
            return fallbackRecommendation(wardrobeItems, occasion, temperature, condition)
        }

        return try {
            Log.d(TAG, "Building prompt with ${wardrobeItems.size} items for occasion: $occasion")
            val prompt = buildPrompt(
                wardrobeItems = wardrobeItems,
                temperature = temperature,
                condition = condition,
                occasion = occasion,
                stylePreference = stylePreference,
                favoriteColors = favoriteColors
            )
            Log.d(TAG, "Prompt built. Length: ${prompt.length}")
            
            Log.d(TAG, "Creating Gemini request...")
            val request = GeminiGenerateContentRequest(
                contents = listOf(
                    GeminiContent(
                        role = "user",
                        parts = listOf(
                            GeminiPart(text = prompt)
                        )
                    )
                )
            )
            
            Log.d(TAG, "Calling geminiService.generateOutfitAI with key: ${apiKey.take(10)}...")
            val response = geminiService.generateOutfitAI(
                apiKey = apiKey,
                request = request
            )
            Log.d(TAG, "✓ API Response received!")
            Log.d(TAG, "Response candidates: ${response.candidates.size}")

            response.toOutfitRecommendation(wardrobeItems, occasion, temperature, condition)
        } catch (e: Exception) {
            Log.e(TAG, "✗ ERROR calling Gemini API: ${e.message}", e)
            e.printStackTrace()
            fallbackRecommendation(wardrobeItems, occasion, temperature, condition)
        }
    }

    private fun buildPrompt(
        wardrobeItems: List<WardrobeItem>,
        temperature: String,
        condition: String,
        occasion: String,
        stylePreference: String?,
        favoriteColors: List<String>
    ): String {
        val groupedItems = wardrobeItems.groupBy { it.category.lowercase() }
        val wardrobeText = buildString {
            appendLine("Wardrobe items grouped by category:")
            groupedItems.forEach { (category, items) ->
                appendLine("- $category")
                items.forEach { item ->
                    appendLine("  • ${item.name} | color=${item.color} | season=${item.season}")
                }
            }
        }

        val preferencesText = buildString {
            appendLine("User occasion: $occasion")
            appendLine("Weather: $temperature and $condition")
            if (!stylePreference.isNullOrBlank()) {
                appendLine("Preferred style: $stylePreference")
            }
            if (favoriteColors.isNotEmpty()) {
                appendLine("Favorite colors: ${favoriteColors.joinToString()}")
            }
        }

        return """
            You are a fashion stylist AI for an Android wardrobe app.
            Choose the best real outfit combination from the wardrobe items below.
            Use the user's occasion, weather, and preferences to decide.

            $preferencesText
            $wardrobeText

            Rules:
            - Return ONLY a JSON object.
            - Use this exact schema:
              {
                "top": "...",
                "bottom": "...",
                "shoes": "...",
                "accessories": "...",
                "description": "..."
              }
            - Pick actual wardrobe items when possible.
            - Keep the description short and helpful.
            - If an item category is missing, choose the best compatible alternative from the wardrobe.
        """.trimIndent()
    }

    private fun GeminiGenerateContentResponse.toOutfitRecommendation(
        wardrobeItems: List<WardrobeItem>,
        occasion: String,
        temperature: String,
        condition: String
    ): OutfitRecommendation {
        val rawText = candidates.firstOrNull()
            ?.content
            ?.parts
            ?.joinToString(separator = "") { it.text }
            .orEmpty()

        val parsed = rawText.toGeneratedOutfitOrNull()
        return if (parsed != null) {
            parsed.copy(rawResponse = rawText)
        } else {
            fallbackRecommendation(wardrobeItems, occasion, temperature, condition)
        }
    }

    private fun String.toGeneratedOutfitOrNull(): OutfitRecommendation? {
        val json = extractJsonObject(this)
        if (json.isBlank()) return null

        return try {
            val obj = JsonParser.parseString(json).asJsonObject
            OutfitRecommendation(
                top = obj.get("top")?.asString.orEmpty(),
                bottom = obj.get("bottom")?.asString.orEmpty(),
                shoes = obj.get("shoes")?.asString.orEmpty(),
                accessories = obj.get("accessories")?.asString.orEmpty(),
                description = obj.get("description")?.asString.orEmpty().ifBlank {
                    "A thoughtful outfit recommendation generated for you."
                },
                rawResponse = this
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun extractJsonObject(text: String): String {
        val fenced = Regex("```(?:json)?\\s*([\\s\\S]*?)\\s*```", RegexOption.IGNORE_CASE)
            .find(text)
            ?.groupValues
            ?.getOrNull(1)
            ?.trim()

        if (!fenced.isNullOrBlank()) return fenced

        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        return if (start >= 0 && end > start) text.substring(start, end + 1).trim() else ""
    }

    private fun fallbackRecommendation(
        wardrobeItems: List<WardrobeItem>,
        occasion: String,
        temperature: String,
        condition: String
    ): OutfitRecommendation {
        return OutfitRecommendation.fallbackFromWardrobe(
            wardrobeItems = wardrobeItems,
            occasion = occasion,
            weatherLabel = "$temperature°C, $condition"
        ).copy(
            rawResponse = gson.toJson(
                mapOf(
                    "fallback" to true,
                    "occasion" to occasion,
                    "weather" to "$temperature°C, $condition"
                )
            )
        )
    }
}


