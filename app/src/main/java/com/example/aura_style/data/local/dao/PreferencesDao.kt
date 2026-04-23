package com.example.aura_style.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aura_style.data.local.entity.Preferences

@Dao
interface PreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreferences(prefs: Preferences)

    @Query("SELECT * FROM preferences LIMIT 1")
    suspend fun getPreferences(): Preferences?

    @Update
    suspend fun updatePreferences(prefs: Preferences)
}

