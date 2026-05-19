package com.example.aura_style.data.repository

import com.example.aura_style.data.local.dao.FavoriteDao
import com.example.aura_style.data.local.entity.Favorite

class FavoriteRepository(
    private val favoriteDao: FavoriteDao
) {
    suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)

    suspend fun updateFavorite(favorite: Favorite) = favoriteDao.updateFavorite(favorite)

    suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.deleteFavorite(favorite)

    suspend fun getAllFavorites(): List<Favorite> = favoriteDao.getAllFavorites()

    suspend fun getFavoriteById(id: Int): Favorite? = favoriteDao.getFavoriteById(id)

    suspend fun getFavoritesCount(): Int = favoriteDao.getFavoritesCount()
}

