package com.example.aura_style

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Functional navigation tests for Aura_Style.
 * Verifies the flow: Login -> Home -> Wardrobe -> Add -> Favorites -> Profile.
 */
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullAppNavigationFlow() {
        // 1. Start at Login Screen and perform login
        composeTestRule.onNodeWithText("Email address").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Sign in").performClick()

        // 2. Verify we land on the Home Screen (check for unique text)
        composeTestRule.onNodeWithText("AI Recommendation").assertIsDisplayed()

        // 3. Navigate to Wardrobe
        composeTestRule.onNodeWithContentDescription("Wardrobe").performClick()
        composeTestRule.onNodeWithText("Wardrobe", substring = true).assertIsDisplayed()

        // 4. Navigate to Add Item
        composeTestRule.onNodeWithContentDescription("Add").performClick()
        composeTestRule.onNodeWithText("Add Wardrobe Item").assertIsDisplayed()

        // 5. Navigate to Favorites
        composeTestRule.onNodeWithContentDescription("Favorites").performClick()
        composeTestRule.onNodeWithText("Favorites", substring = true).assertIsDisplayed()

        // 6. Navigate to Profile (Preferences)
        composeTestRule.onNodeWithContentDescription("Profile").performClick()
        composeTestRule.onNodeWithText("My Profile").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dark Mode").assertIsDisplayed()
        
        // 7. Return Home
        composeTestRule.onNodeWithContentDescription("Home").performClick()
        composeTestRule.onNodeWithText("Good morning, Fashionista").assertIsDisplayed()
    }
}
