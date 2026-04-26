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
import com.example.aura_style.ui.screen.LoginScreen
import com.example.aura_style.ui.screen.RegistrationScreen
import com.example.aura_style.ui.theme.Aura_StyleTheme
import com.example.aura_style.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var showRegistration by rememberSaveable { mutableStateOf(false) }

            Aura_StyleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (showRegistration) {
                        RegistrationScreen(
                            userViewModel = userViewModel,
                            onBackToLoginClick = { showRegistration = false },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        LoginScreen(
                            userViewModel = userViewModel,
                            onSignUpClick = { showRegistration = true },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}
