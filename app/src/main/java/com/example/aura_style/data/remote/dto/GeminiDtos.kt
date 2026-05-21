package com.example.aura_style.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiGenerateContentRequest(
    @SerializedName("contents")
    val contents: List<GeminiContent>,
    @SerializedName("generationConfig")
    val generationConfig: GeminiGenerationConfig = GeminiGenerationConfig()
)

data class GeminiContent(
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("parts")
    val parts: List<GeminiPart>
)

data class GeminiPart(
    @SerializedName("text")
    val text: String
)

data class GeminiGenerationConfig(
    @SerializedName("temperature")
    val temperature: Double = 0.7,
    @SerializedName("topP")
    val topP: Double = 0.95,
    @SerializedName("topK")
    val topK: Int = 40,
    @SerializedName("maxOutputTokens")
    val maxOutputTokens: Int = 512
)

data class GeminiGenerateContentResponse(
    @SerializedName("candidates")
    val candidates: List<GeminiCandidate> = emptyList()
)

data class GeminiCandidate(
    @SerializedName("content")
    val content: GeminiResponseContent? = null,
    @SerializedName("finishReason")
    val finishReason: String? = null
)

data class GeminiResponseContent(
    @SerializedName("role")
    val role: String? = null,
    @SerializedName("parts")
    val parts: List<GeminiPart> = emptyList()
)
