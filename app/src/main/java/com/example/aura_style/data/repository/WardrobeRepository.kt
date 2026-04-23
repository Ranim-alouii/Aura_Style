package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.WardrobeDao
import com.example.aura_style.data.local.entity.WardrobeItem
import javax.inject.Inject

class WardrobeRepository @Inject constructor(
    private val wardrobeDao: WardrobeDao
) {
    suspend fun insertItem(item: WardrobeItem) = wardrobeDao.insertItem(item)

    suspend fun updateItem(item: WardrobeItem) = wardrobeDao.updateItem(item)

    suspend fun deleteItem(item: WardrobeItem) = wardrobeDao.deleteItem(item)

    suspend fun getAllItems(): List<WardrobeItem> = wardrobeDao.getAllItems()

    suspend fun getItemsByCategory(category: String): List<WardrobeItem> =
        wardrobeDao.getItemsByCategory(category)
}



