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

    init {
        getPreferences()
    }

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
            _preferences.value = prefs
        }
    }

    fun updateStyle(style: String) {
        val currentPrefs = _preferences.value ?: Preferences()
        val updatedPrefs = currentPrefs.copy(style = style)
        updatePreferences(updatedPrefs)
    }

    fun updateFavoriteColors(colors: List<String>) {
        val currentPrefs = _preferences.value ?: Preferences()
        val updatedPrefs = currentPrefs.copy(favoriteColors = colors)
        updatePreferences(updatedPrefs)
    }

    fun toggleWeatherSensitive() {
        val currentPrefs = _preferences.value ?: Preferences()
        val updatedPrefs = currentPrefs.copy(weatherSensitive = !currentPrefs.weatherSensitive)
        updatePreferences(updatedPrefs)
    }

    fun toggleNotifications() {
        val currentPrefs = _preferences.value ?: Preferences()
        val updatedPrefs = currentPrefs.copy(notificationsEnabled = !currentPrefs.notificationsEnabled)
        updatePreferences(updatedPrefs)
    }

    fun togglePrivacySettings() {
        val currentPrefs = _preferences.value ?: Preferences()
        val updatedPrefs = currentPrefs.copy(privacySettingsEnabled = !currentPrefs.privacySettingsEnabled)
        updatePreferences(updatedPrefs)
    }
}

