package com.example.aura_style.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit_logs")
data class OutfitLog(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val itemsUsed: String
)
