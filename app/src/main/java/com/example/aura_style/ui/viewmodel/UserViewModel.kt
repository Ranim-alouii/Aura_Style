package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.User
import com.example.aura_style.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> = _authMessage.asStateFlow()

    fun insertUser(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = "Email and password are required"
            return
        }

        viewModelScope.launch {
            val user = User(email = email.trim(), password = password)
            userRepository.insertUser(user)
            _user.value = userRepository.getUserByEmail(user.email)
            _authMessage.value = "Registration successful"
        }
    }

    fun getUserByEmail(email: String, passwordToCheck: String? = null) {
        if (email.isBlank()) {
            _authMessage.value = "Email is required"
            return
        }

        viewModelScope.launch {
            val foundUser = userRepository.getUserByEmail(email.trim())
            _user.value = foundUser

            if (passwordToCheck != null) {
                _authMessage.value = when {
                    foundUser == null -> "User not found"
                    foundUser.password == passwordToCheck -> "Login successful"
                    else -> "Invalid password"
                }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            if (_user.value?.id == user.id) {
                _user.value = null
                _authMessage.value = "User deleted"
            }
        }
    }
}