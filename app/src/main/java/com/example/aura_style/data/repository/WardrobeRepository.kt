package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.WardrobeDao
import com.example.aura_style.data.local.entity.WardrobeItem

open class WardrobeRepository(
    private val wardrobeDao: WardrobeDao
) {
    open suspend fun insertItem(item: WardrobeItem) = wardrobeDao.insertItem(item)
    open suspend fun updateItem(item: WardrobeItem) = wardrobeDao.updateItem(item)
    open suspend fun deleteItem(item: WardrobeItem) = wardrobeDao.deleteItem(item)
    open suspend fun getAllItems(): List<WardrobeItem> = wardrobeDao.getAllItems()
    open suspend fun getItemsByCategory(category: String): List<WardrobeItem> =
        wardrobeDao.getItemsByCategory(category)
}
