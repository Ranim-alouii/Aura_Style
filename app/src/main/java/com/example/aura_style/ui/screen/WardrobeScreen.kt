package com.example.aura_style.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.aura_style.data.local.entity.WardrobeItem
import com.example.aura_style.ui.viewmodel.WardrobeViewModel

private val PurpleAccent = Color(0xFF7E57C2)
private val LightPurple = Color(0xFFF3E5F5)
private val SearchFieldBackground = Color(0xFFF8F5FC)

private val CategoryFilters = listOf("All", "Tops", "Bottoms", "Shoes")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WardrobeScreen(
    wardrobeViewModel: WardrobeViewModel,
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {},
    onNavigateToWardrobe: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Log.d("WardrobeScreen", "WardrobeScreen composable called")
    var selectedNavIndex by remember { mutableIntStateOf(1) }

    // Use produceState to safely collect flows and provide fallbacks on error.
    val searchQuery by produceState(initialValue = "", key1 = wardrobeViewModel) {
        try {
            wardrobeViewModel.searchQuery.collect { value = it }
        } catch (_: Throwable) {
            value = ""
        }
    }

    val selectedCategory by produceState(initialValue = "All", key1 = wardrobeViewModel) {
        try {
            wardrobeViewModel.selectedCategory.collect { value = it }
        } catch (_: Throwable) {
            value = "All"
        }
    }

    val items by produceState(initialValue = emptyList<com.example.aura_style.data.local.entity.WardrobeItem>(), key1 = wardrobeViewModel) {
        try {
            wardrobeViewModel.filteredItems.collect { value = it }
        } catch (_: Throwable) {
            value = emptyList()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Wardrobe",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )
                        Text(
                            text = "${items.size} items",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF616161)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF212121)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedNavIndex == 0,
                    onClick = {
                        selectedNavIndex = 0
                        onNavigateToHome()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Wardrobe") },
                    label = { Text("Wardrobe") },
                    selected = selectedNavIndex == 1,
                    onClick = {
                        selectedNavIndex = 1
                        onNavigateToWardrobe()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                    label = { Text("Add") },
                    selected = selectedNavIndex == 2,
                    onClick = {
                        selectedNavIndex = 2
                        onNavigateToAdd()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") },
                    selected = selectedNavIndex == 3,
                    onClick = {
                        selectedNavIndex = 3
                        onNavigateToFavorites()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedNavIndex == 4,
                    onClick = {
                        selectedNavIndex = 4
                        onNavigateToProfile()
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = wardrobeViewModel::updateSearchQuery,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search your wardrobe…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                trailingIcon = {
                    IconButton(onClick = wardrobeViewModel::clearFilters) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CategoryFilters.forEach { category ->
                    FilterChip(
                        selected = selectedCategory.equals(category, ignoreCase = true),
                        onClick = { wardrobeViewModel.updateCategoryFilter(category) },
                        label = { Text(category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleAccent,
                            selectedLabelColor = Color.White,
                            containerColor = SearchFieldBackground,
                            labelColor = Color(0xFF212121)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (items.isEmpty()) {
                EmptyWardrobeState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items, key = { index, item ->
                        if (item.id != 0) item.id else "sample-${index}-${item.name}-${item.category}"
                    }) { _, item ->
                        WardrobeItemCard(item = item)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun WardrobeItemCard(item: WardrobeItem) {
    val swatchColor = colorForName(item.color)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = LightPurple,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(swatchColor, CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF212121),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.color,
                style = MaterialTheme.typography.bodyMedium,
                color = PurpleAccent,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = item.category,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun EmptyWardrobeState() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No items found",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF212121),
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Try a different search or clear your filters.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF616161),
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

private fun colorForName(colorName: String): Color {
    return when (colorName.trim().lowercase()) {
        "purple" -> Color(0xFF7E57C2)
        "blue" -> Color(0xFF2196F3)
        "green" -> Color(0xFF4CAF50)
        "yellow" -> Color(0xFFFFC107)
        "orange" -> Color(0xFFFF9800)
        "red" -> Color(0xFFF44336)
        "white" -> Color(0xFFF5F5F5)
        "black" -> Color(0xFF212121)
        else -> PurpleAccent
    }
}






