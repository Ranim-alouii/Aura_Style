package com.example.aura_style.data.remote

import com.example.aura_style.BuildConfig
import com.example.aura_style.data.remote.dto.GeminiGenerateContentRequest
import com.example.aura_style.data.remote.dto.GeminiGenerateContentResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiService {
    @POST("v1/models/gemini-1.5-flash:generateContent")
    suspend fun generateOutfitAI(
        @Query("key") apiKey: String = BuildConfig.GEMINI_API_KEY,
        @Body request: GeminiGenerateContentRequest
    ): GeminiGenerateContentResponse
}
