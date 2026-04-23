package com.example.aura_style.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.aura_style.data.local.StringListConverter

@Entity(tableName = "preferences")
@TypeConverters(StringListConverter::class)
data class Preferences(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val style: String,
    val favoriteColors: List<String>
)

