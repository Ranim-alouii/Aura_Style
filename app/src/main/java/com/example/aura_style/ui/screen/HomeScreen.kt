package com.example.aura_style.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aura_style.ui.viewmodel.OutfitRecommendationViewModel

private val PurpleAccent = Color(0xFF7E57C2)
private val LightPurple = Color(0xFFF3E5F5)

@Composable
fun HomeScreen(
    outfitRecommendationViewModel: OutfitRecommendationViewModel,
    onNavigateToAdd: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedNavIndex by remember { mutableIntStateOf(0) }
    
    val weather by outfitRecommendationViewModel.weather.collectAsState()
    val selectedOccasion by outfitRecommendationViewModel.selectedOccasion.collectAsState()
    val recommendedOutfit by outfitRecommendationViewModel.recommendedOutfit.collectAsState()
    val loading by outfitRecommendationViewModel.loading.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content area with scroll
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                // Greeting
                Text(
                    text = "Good morning, Fashionista",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Let's create the perfect look for today",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF616161),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Weather card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = LightPurple,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = weather.temperature,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = PurpleAccent,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = weather.condition,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF616161),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            Text(
                                text = weather.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF9575CD),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Occasion selector
                Text(
                    text = "What's the occasion?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.SemiBold
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("Casual", "Work", "Sport", "Party")) { occasion ->
                        FilterChip(
                            selected = occasion == selectedOccasion,
                            onClick = { outfitRecommendationViewModel.selectOccasion(occasion) },
                            label = { Text(occasion) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleAccent,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFFE8E8E8),
                                labelColor = Color(0xFF212121)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // AI Recommendation section
                Text(
                    text = "AI Recommendation",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.SemiBold
                )

                if (loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PurpleAccent)
                    }
                } else {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        color = LightPurple,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Recommended Outfit",
                                style = MaterialTheme.typography.titleSmall,
                                color = PurpleAccent,
                                fontWeight = FontWeight.Bold
                            )

                            Column(
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                recommendedOutfit.items.forEach { item ->
                                    Row(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(PurpleAccent, RoundedCornerShape(2.dp))
                                        )
                                        Text(
                                            text = item,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF212121),
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }
                            }

                            Text(
                                text = recommendedOutfit.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF616161),
                                modifier = Modifier.padding(top = 12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Generate New button
                Button(
                    onClick = { outfitRecommendationViewModel.generateRecommendation() },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Generate New", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Bottom Navigation Bar
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedNavIndex == 0,
                    onClick = { selectedNavIndex = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Wardrobe") },
                    label = { Text("Wardrobe") },
                    selected = selectedNavIndex == 1,
                    onClick = {
                        selectedNavIndex = 1
                        onNavigateToSearch()
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
    }
}

