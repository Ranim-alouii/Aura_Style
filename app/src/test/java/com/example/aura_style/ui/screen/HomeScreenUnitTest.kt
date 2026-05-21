package com.example.aura_style.ui.screen

import com.example.aura_style.data.model.OutfitRecommendation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Lightweight unit tests for outfit recommendation data used by HomeScreen.
 * These are pure JVM tests and do not require Compose instrumentation.
 */
class HomeScreenUnitTest {

    @Test
    fun outfitRecommendation_exposesNonBlankItems() {
        val outfit = OutfitRecommendation(
            top = "Blue Blazer",
            bottom = "Black Pants",
            shoes = "Black Dress Shoes",
            accessories = "Silver Watch",
            description = "Professional work outfit perfect for meetings"
        )

        assertNotNull(outfit)
        assertEquals("Blue Blazer", outfit.top)
        assertEquals(4, outfit.items.size)
        assertTrue(outfit.items.contains("Black Pants"))
    }

    @Test
    fun outfitRecommendation_itemsIgnoreBlanks() {
        val outfit = OutfitRecommendation(
            top = "Red Dress",
            bottom = "",
            shoes = "Heels",
            accessories = "",
            description = "Evening look"
        )

        assertEquals(2, outfit.items.size)
        assertTrue(outfit.items.contains("Red Dress"))
        assertTrue(outfit.items.contains("Heels"))
        assertFalse(outfit.items.contains(""))
    }

    @Test
    fun outfitRecommendation_copyRetainsData() {
        val outfit = OutfitRecommendation(
            top = "Favorite Blazer",
            bottom = "Favorite Pants",
            shoes = "Favorite Shoes",
            accessories = "Favorite Watch",
            description = "My favorite work outfit"
        )

        val copied = outfit.copy()

        assertEquals(outfit.top, copied.top)
        assertEquals(outfit.description, copied.description)
    }
}


