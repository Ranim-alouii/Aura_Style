package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.OutfitLogDao
import com.example.aura_style.data.local.entity.OutfitLog
import javax.inject.Inject

class OutfitLogRepository @Inject constructor(
    private val outfitLogDao: OutfitLogDao
) {
    suspend fun insertLog(log: OutfitLog) = outfitLogDao.insertLog(log)

    suspend fun getAllLogs(): List<OutfitLog> = outfitLogDao.getAllLogs()

    suspend fun getLogsByDate(date: String): List<OutfitLog> = outfitLogDao.getLogsByDate(date)
}

