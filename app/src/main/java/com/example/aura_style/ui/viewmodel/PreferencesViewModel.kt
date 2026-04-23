package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.Preferences
import com.example.aura_style.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _preferences = MutableStateFlow<Preferences?>(null)
    val preferences: StateFlow<Preferences?> = _preferences.asStateFlow()

    fun insertPreferences(prefs: Preferences) {
        viewModelScope.launch {
            preferencesRepository.insertPreferences(prefs)
            getPreferences()
        }
    }

    fun getPreferences() {
        viewModelScope.launch {
            _preferences.value = preferencesRepository.getPreferences()
        }
    }

    fun updatePreferences(prefs: Preferences) {
        viewModelScope.launch {
            preferencesRepository.updatePreferences(prefs)
            _preferences.value = preferencesRepository.getPreferences()
        }
    }
}

