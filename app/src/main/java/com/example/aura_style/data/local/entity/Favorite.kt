package com.example.aura_style.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorite(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val outfitName: String,
    val occasion: String,
    val clothingItems: String, // JSON or comma-separated list of item names
    val savedDate: String,
    val isFavorited: Boolean = true
)

