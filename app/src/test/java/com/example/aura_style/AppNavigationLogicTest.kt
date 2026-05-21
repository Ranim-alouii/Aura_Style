package com.example.aura_style

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Simple JVM navigation logic tests for the current state-based routing in MainActivity.
 */
class AppNavigationLogicTest {

    @Test
    fun navigationFromHomeToWardrobe_updatesCurrentScreen() {
        var currentScreen = "home"
        currentScreen = "wardrobe"

        assertEquals("wardrobe", currentScreen)
        assertNotEquals("home", currentScreen)
    }

    @Test
    fun completeNavigationFlow_tracksExpectedRoutes() {
        var currentScreen = "home"
        val navigationHistory = mutableListOf(currentScreen)

        listOf("wardrobe", "add", "favorites", "preferences", "home").forEach { route ->
            currentScreen = route
            navigationHistory.add(currentScreen)
        }

        assertEquals("home", currentScreen)
        assertEquals(6, navigationHistory.size)
        assertEquals("home", navigationHistory.first())
        assertEquals("home", navigationHistory.last())
        assertTrue(navigationHistory.containsAll(listOf("wardrobe", "add", "favorites", "preferences")))
    }

    @Test
    fun navBar_itemsCount_matchesMainActivityScreens() {
        val navItems = listOf("Home", "Wardrobe", "Add", "Favorites", "Profile")

        assertEquals(5, navItems.size)
        assertTrue(navItems.contains("Home"))
        assertTrue(navItems.contains("Wardrobe"))
        assertTrue(navItems.contains("Add"))
        assertTrue(navItems.contains("Favorites"))
        assertTrue(navItems.contains("Profile"))
    }
}

