package com.example.aura_style.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.aura_style.ui.viewmodel.PreferencesViewModel
import com.example.aura_style.ui.viewmodel.UserViewModel

private val PurpleAccent = Color(0xFF7E57C2)
private val LightPurple = Color(0xFFF3E5F5)

// Color palette for favorite colors section
private val ColorPalette = listOf(
    Color(0xFF7E57C2),  // Purple
    Color(0xFF2196F3),  // Blue
    Color(0xFF4CAF50),  // Green
    Color(0xFFFFC107),  // Yellow
    Color(0xFFFF9800),  // Orange
    Color(0xFFF44336)   // Red
)

private val ColorNames = listOf("Purple", "Blue", "Green", "Yellow", "Orange", "Red")

@Composable
fun PreferencesScreen(
    preferencesViewModel: PreferencesViewModel,
    userViewModel: UserViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToWardrobe: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedNavIndex by remember { mutableIntStateOf(4) }
    
    val preferences by preferencesViewModel.preferences.collectAsState()
    val currentUser by userViewModel.user.collectAsState()

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
                // Profile Header
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Profile Info Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = LightPurple,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(PurpleAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentUser?.email?.firstOrNull()?.uppercase() ?: "U"),
                                style = MaterialTheme.typography.headlineSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentUser?.email?.substringBefore("@") ?: "User",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF212121),
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = currentUser?.email ?: "user@example.com",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF616161),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard("24", "Outfits")
                    StatCard("89", "Items")
                    StatCard("12", "Favorites")
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Style Preference Section
                Text(
                    text = "Style Preference",
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
                    items(listOf("Casual", "Elegant", "Streetwear", "Sporty")) { style ->
                        FilterChip(
                            selected = style == preferences?.style,
                            onClick = { preferencesViewModel.updateStyle(style) },
                            label = { Text(style) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleAccent,
                                selectedLabelColor = Color.White,
                                containerColor = Color(0xFFE8E8E8),
                                labelColor = Color(0xFF212121)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Favorite Colors Section
                Text(
                    text = "Favorite Colors",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Select your preferred color palette",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF616161),
                    modifier = Modifier.padding(top = 4.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(ColorPalette.size) { index ->
                        ColorPickerCircle(
                            color = ColorPalette[index],
                            colorName = ColorNames[index],
                            isSelected = preferences?.favoriteColors?.contains(ColorNames[index]) ?: false,
                            onColorSelected = { isSelected ->
                                val currentColors = preferences?.favoriteColors?.toMutableList() ?: mutableListOf()
                                if (isSelected) {
                                    if (!currentColors.contains(ColorNames[index])) {
                                        currentColors.add(ColorNames[index])
                                    }
                                } else {
                                    currentColors.remove(ColorNames[index])
                                }
                                preferencesViewModel.updateFavoriteColors(currentColors)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Settings Section
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Weather Sensitive Toggle
                SettingRow(
                    title = "Weather Sensitive",
                    description = "AI considers weather in recommendations",
                    isEnabled = preferences?.weatherSensitive ?: true,
                    onToggle = { preferencesViewModel.toggleWeatherSensitive() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notifications Toggle
                SettingRow(
                    title = "Notifications",
                    description = "Daily outfit suggestions",
                    isEnabled = preferences?.notificationsEnabled ?: true,
                    onToggle = { preferencesViewModel.toggleNotifications() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy Settings
                SettingRowWithArrow(
                    title = "Privacy Settings",
                    description = "Control your data and privacy",
                    isEnabled = preferences?.privacySettingsEnabled ?: false,
                    onToggle = { preferencesViewModel.togglePrivacySettings() }
                )

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
                    onClick = { selectedNavIndex = 4 }
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, PurpleAccent, RoundedCornerShape(12.dp)),
        color = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = PurpleAccent,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF616161),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ColorPickerCircle(
    color: Color,
    colorName: String,
    isSelected: Boolean,
    onColorSelected: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .background(color, CircleShape)
            .border(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) PurpleAccent else Color.LightGray,
                shape = CircleShape
            )
            .clickable { onColorSelected(!isSelected) },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = colorName,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPurple, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF212121),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PurpleAccent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCCC2D9)
            )
        )
    }
}

@Composable
private fun SettingRowWithArrow(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightPurple, RoundedCornerShape(12.dp))
            .clickable { onToggle() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF212121),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF616161),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PurpleAccent,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFCCC2D9)
                )
            )
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "More options",
                tint = PurpleAccent,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}

