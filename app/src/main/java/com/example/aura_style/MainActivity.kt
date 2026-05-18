package com.example.aura_style

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.aura_style.ui.screen.AddItemScreen
import com.example.aura_style.ui.screen.HomeScreen
import com.example.aura_style.ui.screen.LoginScreen
import com.example.aura_style.ui.screen.PreferencesScreen
import com.example.aura_style.ui.screen.RegistrationScreen
import com.example.aura_style.ui.theme.Aura_StyleTheme
import com.example.aura_style.ui.viewmodel.OutfitRecommendationViewModel
import com.example.aura_style.ui.viewmodel.PreferencesViewModel
import com.example.aura_style.ui.viewmodel.UserViewModel
import com.example.aura_style.ui.viewmodel.WardrobeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private val outfitRecommendationViewModel: OutfitRecommendationViewModel by viewModels()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
    private val wardrobeViewModel: WardrobeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showRegistration by rememberSaveable { mutableStateOf(false) }
            var isLoggedIn by rememberSaveable { mutableStateOf(false) }
            var currentScreen by rememberSaveable { mutableStateOf("home") } // home, preferences, add, favorites, search

            Aura_StyleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when {
                        !isLoggedIn && !showRegistration -> {
                            LoginScreen(
                                userViewModel = userViewModel,
                                onSignUpClick = { showRegistration = true },
                                onLoginSuccess = { isLoggedIn = true },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        !isLoggedIn && showRegistration -> {
                            RegistrationScreen(
                                userViewModel = userViewModel,
                                onBackToLoginClick = { showRegistration = false },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        isLoggedIn && currentScreen == "preferences" -> {
                            PreferencesScreen(
                                preferencesViewModel = preferencesViewModel,
                                userViewModel = userViewModel,
                                onNavigateToHome = { currentScreen = "home" },
                                onNavigateToWardrobe = { currentScreen = "wardrobe" },
                                onNavigateToAdd = { currentScreen = "add" },
                                onNavigateToFavorites = { currentScreen = "favorites" },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        isLoggedIn && currentScreen == "add" -> {
                            AddItemScreen(
                                wardrobeViewModel = wardrobeViewModel,
                                userViewModel = userViewModel,
                                onNavigateToHome = { currentScreen = "home" },
                                onNavigateToWardrobe = { currentScreen = "wardrobe" },
                                onNavigateToFavorites = { currentScreen = "favorites" },
                                onNavigateToProfile = { currentScreen = "preferences" },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        else -> {
                            HomeScreen(
                                outfitRecommendationViewModel = outfitRecommendationViewModel,
                                onNavigateToAdd = { currentScreen = "add" },
                                onNavigateToFavorites = { currentScreen = "favorites" },
                                onNavigateToSearch = { currentScreen = "search" },
                                onNavigateToProfile = { currentScreen = "preferences" },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
