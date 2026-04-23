package com.example.aura_style.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aura_style.data.local.entity.WardrobeItem

@Dao
interface WardrobeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: WardrobeItem)

    @Update
    suspend fun updateItem(item: WardrobeItem)

    @Delete
    suspend fun deleteItem(item: WardrobeItem)

    @Query("SELECT * FROM wardrobe_items")
    suspend fun getAllItems(): List<WardrobeItem>

    @Query("SELECT * FROM wardrobe_items WHERE category = :category")
    suspend fun getItemsByCategory(category: String): List<WardrobeItem>
}

