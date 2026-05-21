package com.example.aura_style.ui.viewmodel

import android.util.Log
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

private const val TAG = "WardrobeViewModel"

@HiltViewModel
open class WardrobeViewModel @Inject constructor(
    private val wardrobeRepository: WardrobeRepository
) : ViewModel() {
    private val sampleItems = listOf(
        WardrobeItem(name = "White Shirt", category = "Tops", color = "White", season = "Casual"),
        WardrobeItem(name = "Black Jeans", category = "Bottoms", color = "Black", season = "Casual"),
        WardrobeItem(name = "Sneakers", category = "Shoes", color = "White", season = "Sporty"),
        WardrobeItem(name = "Summer Dress", category = "Tops", color = "Blue", season = "Elegant"),
        WardrobeItem(name = "Leather Jacket", category = "Tops", color = "Black", season = "Streetwear"),
        WardrobeItem(name = "Red Heels", category = "Shoes", color = "Red", season = "Elegant")
    )

    private val _items = MutableStateFlow<List<WardrobeItem>>(emptyList())
    val items: StateFlow<List<WardrobeItem>> = _items.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _filteredItems = MutableStateFlow<List<WardrobeItem>>(sampleItems)
    val filteredItems: StateFlow<List<WardrobeItem>> = _filteredItems.asStateFlow()

    init {
        Log.d(TAG, "WardrobeViewModel initialized")
        refreshFilteredItems()
        getAllItems()
    }

    fun insertItem(item: WardrobeItem) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Inserting item: ${item.name}")
                wardrobeRepository.insertItem(item)
                Log.d(TAG, "Item inserted successfully")
                getAllItems()
            } catch (t: Throwable) {
                Log.e(TAG, "Error inserting item", t)
            }
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
            try {
                Log.d(TAG, "Fetching all wardrobe items...")
                val items = wardrobeRepository.getAllItems()
                Log.d(TAG, "Successfully fetched ${items.size} items")
                _items.value = items
            } catch (t: Throwable) {
                Log.e(TAG, "Error fetching items from repository", t)
                // if DB read fails, fall back to empty list to avoid crashing UI
                _items.value = emptyList()
            }
            try {
                refreshFilteredItems()
            } catch (t: Throwable) {
                Log.e(TAG, "Error refreshing filters", t)
            }
        }
    }

    fun getItemsByCategory(category: String) {
        viewModelScope.launch {
            try {
                _items.value = wardrobeRepository.getItemsByCategory(category)
            } catch (t: Throwable) {
                _items.value = emptyList()
            }
            _selectedCategory.value = category
            try {
                refreshFilteredItems()
            } catch (_: Throwable) {
                // ignore
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        refreshFilteredItems()
    }

    fun updateCategoryFilter(category: String) {
        _selectedCategory.value = category
        refreshFilteredItems()
    }

    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = "All"
        refreshFilteredItems()
    }

    private fun refreshFilteredItems() {
        val source = if (_items.value.isNotEmpty()) _items.value else sampleItems
        val query = _searchQuery.value.trim()
        val category = _selectedCategory.value

        _filteredItems.value = source.filter { item ->
            val matchesQuery = query.isBlank() ||
                item.name.contains(query, ignoreCase = true) ||
                item.category.contains(query, ignoreCase = true) ||
                item.color.contains(query, ignoreCase = true) ||
                item.season.contains(query, ignoreCase = true)

            val matchesCategory = category == "All" || item.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }
}
