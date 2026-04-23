package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.PreferencesDao
import com.example.aura_style.data.local.entity.Preferences
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferencesDao: PreferencesDao
) {
    suspend fun insertPreferences(prefs: Preferences) = preferencesDao.insertPreferences(prefs)

    suspend fun getPreferences(): Preferences? = preferencesDao.getPreferences()

    suspend fun updatePreferences(prefs: Preferences) = preferencesDao.updatePreferences(prefs)
}



