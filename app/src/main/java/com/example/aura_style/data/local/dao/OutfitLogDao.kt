package com.example.aura_style.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aura_style.data.local.entity.OutfitLog

@Dao
interface OutfitLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: OutfitLog)

    @Query("SELECT * FROM outfit_logs")
    suspend fun getAllLogs(): List<OutfitLog>

    @Query("SELECT * FROM outfit_logs WHERE date = :date")
    suspend fun getLogsByDate(date: String): List<OutfitLog>
}

