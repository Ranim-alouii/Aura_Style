package com.example.aura_style.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.data.repository.WardrobeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class WardrobeViewModel @Inject constructor(
    private val wardrobeRepository: WardrobeRepository
) : ViewModel() {
    private val _items = MutableStateFlow<List<WardrobeItem>>(emptyList())
    val items: StateFlow<List<WardrobeItem>> = _items.asStateFlow()

    fun insertItem(item: WardrobeItem) {
        viewModelScope.launch {
            wardrobeRepository.insertItem(item)
            getAllItems()
        }
    }

    fun insertItem(
        name: String,
        category: String,
        selectedStyle: String,
        selectedColor: String,
        photoUri: String? = null
    ) {
        insertItem(
            WardrobeItem(
                name = name.trim(),
                category = category.trim(),
                color = selectedColor,
                season = selectedStyle,
                photoUri = photoUri
            )
        )
    }

    fun updateItem(item: WardrobeItem) {
        viewModelScope.launch {
            wardrobeRepository.updateItem(item)
            getAllItems()
        }
    }

    fun deleteItem(item: WardrobeItem) {
        viewModelScope.launch {
            wardrobeRepository.deleteItem(item)
            getAllItems()
        }
    }

    fun getAllItems() {
        viewModelScope.launch {
            _items.value = wardrobeRepository.getAllItems()
        }
    }

    fun getItemsByCategory(category: String) {
        viewModelScope.launch {
            _items.value = wardrobeRepository.getItemsByCategory(category)
        }
    }
}

