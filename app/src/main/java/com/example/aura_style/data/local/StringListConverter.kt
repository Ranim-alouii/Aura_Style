package com.example.aura_style.data.local

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromList(value: List<String>): String = value.joinToString(separator = "||")

    @TypeConverter
    fun toList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return value.split("||")
    }
}

