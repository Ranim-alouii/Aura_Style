package com.example.aura_style.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @field:PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val password: String
)

