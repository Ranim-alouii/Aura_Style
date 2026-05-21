package com.example.aura_style.data.model

import com.example.aura_style.data.local.entity.WardrobeItem

data class OutfitRecommendation(
    val top: String = "",
    val bottom: String = "",
    val shoes: String = "",
    val accessories: String = "",
    val description: String = "A great outfit choice for today.",
    val rawResponse: String = ""
) {
    val items: List<String>
        get() = listOf(top, bottom, shoes, accessories).filter { it.isNotBlank() }

    fun toLogString(occasion: String, weatherLabel: String): String {
        return buildString {
            append("Occasion: ").append(occasion)
            append(" | Weather: ").append(weatherLabel)
            append(" | Top: ").append(top.ifBlank { "N/A" })
            append(" | Bottom: ").append(bottom.ifBlank { "N/A" })
            append(" | Shoes: ").append(shoes.ifBlank { "N/A" })
            append(" | Accessories: ").append(accessories.ifBlank { "N/A" })
        }
    }

    companion object {
        fun fallbackFromWardrobe(
            wardrobeItems: List<WardrobeItem>,
            occasion: String,
            weatherLabel: String
        ): OutfitRecommendation {
            val grouped = wardrobeItems.groupBy { it.category.lowercase() }
            fun pick(vararg keywords: String): String =
                wardrobeItems.firstOrNull { item ->
                    keywords.any { keyword ->
                        item.category.contains(keyword, ignoreCase = true) ||
                            item.name.contains(keyword, ignoreCase = true)
                    }
                }?.name.orEmpty()

            val top = pick("top", "shirt", "tee", "t-shirt", "blouse", "jacket", "sweater")
                .ifBlank { grouped.values.flatten().firstOrNull()?.name.orEmpty() }
            val bottom = pick("bottom", "pant", "jean", "trouser", "skirt", "short")
                .ifBlank { grouped.values.flatten().getOrNull(1)?.name.orEmpty() }
            val shoes = pick("shoe", "sneaker", "boot", "loafer", "heel", "sandal")
                .ifBlank { grouped.values.flatten().getOrNull(2)?.name.orEmpty() }
            val accessories = pick("accessory", "bag", "watch", "belt", "scarf", "hat", "jewelry")
                .ifBlank { grouped.values.flatten().getOrNull(3)?.name.orEmpty() }

            val description = when (occasion.lowercase()) {
                "work" -> "A professional look with a polished silhouette for your work day."
                "party" -> "A standout outfit that balances style and confidence for the event."
                "sport", "sports", "sporty" -> "A comfortable activewear-inspired outfit suited for movement."
                else -> "A balanced outfit tailored for today’s weather and your wardrobe."
            } + if (weatherLabel.isNotBlank()) " Weather: $weatherLabel." else ""

            return OutfitRecommendation(
                top = top,
                bottom = bottom,
                shoes = shoes,
                accessories = accessories,
                description = description
            )
        }
    }
}
