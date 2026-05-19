package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.Favorite
import com.example.aura_style.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites.asStateFlow()

    private val _favoritesCount = MutableStateFlow(0)
    val favoritesCount: StateFlow<Int> = _favoritesCount.asStateFlow()

    init {
        getAllFavorites()
    }

    fun getAllFavorites() {
        viewModelScope.launch {
            _favorites.value = favoriteRepository.getAllFavorites()
            _favoritesCount.value = favoriteRepository.getFavoritesCount()
        }
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favorite)
            getAllFavorites()
        }
    }

    fun toggleFavorite(favorite: Favorite) {
        val updated = favorite.copy(isFavorited = !favorite.isFavorited)
        viewModelScope.launch {
            favoriteRepository.updateFavorite(updated)
            getAllFavorites()
        }
    }

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.insertFavorite(favorite)
            getAllFavorites()
        }
    }
}

