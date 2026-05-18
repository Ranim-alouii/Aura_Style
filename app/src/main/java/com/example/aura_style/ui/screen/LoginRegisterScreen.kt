package com.example.aura_style.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.aura_style.ui.viewmodel.UserViewModel

private val PurpleAccent = Color(0xFF7E57C2)

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    onSignUpClick: () -> Unit,
    onLoginSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
     var email by remember { mutableStateOf("") }
     var password by remember { mutableStateOf("") }

     val authMessage by userViewModel.authMessage.collectAsState()

     LaunchedEffect(authMessage) {
         if (authMessage.contains("Login successful", ignoreCase = true)) {
             onLoginSuccess()
         }
     }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Top placeholder icon",
                tint = PurpleAccent,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .background(Color(0xFFF3E5F5), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sign in to your wardrobe",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF616161),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                shape = RoundedCornerShape(14.dp)
            )

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        // Show error - but this is handled by the ViewModel now
                    } else {
                        userViewModel.getUserByEmail(email, password)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp)
            ) {
                Text("Sign in")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Google icon placeholder",
                    tint = PurpleAccent
                )
                Text(
                    text = "Continue with Google",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF424242),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            if (authMessage.isNotBlank()) {
                Text(
                    text = authMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (authMessage.contains("successful", ignoreCase = true)) Color(0xFF2E7D32) else Color(0xFFC62828),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color(0xFF616161),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sign up",
                    color = PurpleAccent,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }
    }
}

@Composable
fun RegistrationScreen(
    userViewModel: UserViewModel,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authMessage by userViewModel.authMessage.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create account",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )

            Button(
                onClick = { userViewModel.insertUser(email, password) },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Register")
            }

            if (authMessage.isNotBlank()) {
                Text(
                    text = authMessage,
                    modifier = Modifier.padding(top = 12.dp),
                    color = if (authMessage.contains("successful")) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }

            Text(
                text = "Back to sign in",
                color = PurpleAccent,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .clickable { onBackToLoginClick() }
            )
        }
    }
}
