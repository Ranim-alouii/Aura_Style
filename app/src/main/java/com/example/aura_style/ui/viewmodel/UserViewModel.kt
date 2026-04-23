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

    fun insertUser(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
            _user.value = userRepository.getUserByEmail(user.email)
        }
    }

    fun getUserByEmail(email: String) {
        viewModelScope.launch {
            _user.value = userRepository.getUserByEmail(email)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user)
            if (_user.value?.id == user.id) {
                _user.value = null
            }
        }
    }
}

