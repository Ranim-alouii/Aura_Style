package com.example.aura_style.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aura_style.ui.viewmodel.FavoritesViewModel
import com.example.aura_style.ui.viewmodel.PreferencesViewModel
import com.example.aura_style.ui.viewmodel.UserViewModel
import com.example.aura_style.ui.viewmodel.WardrobeViewModel

// Representational colors for the palette picker
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
    wardrobeViewModel: WardrobeViewModel,
    favoritesViewModel: FavoritesViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToWardrobe: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedNavIndex by remember { mutableIntStateOf(4) }
    
    val preferences by preferencesViewModel.preferences.collectAsState()
    val currentUser by userViewModel.user.collectAsState()
    val wardrobeItems by wardrobeViewModel.items.collectAsState()
    val favoritesCount by favoritesViewModel.favoritesCount.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Profile Info Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (currentUser?.email?.firstOrNull()?.uppercase() ?: "U"),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentUser?.email?.substringBefore("@") ?: "User",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = currentUser?.email ?: "user@example.com",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Row - Pulling real counts from DB
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard("0", "Outfits")
                    StatCard(wardrobeItems.size.toString(), "Items")
                    StatCard(favoritesCount.toString(), "Favorites")
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Style Preference",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
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
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Favorite Colors",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Select your preferred color palette",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingRow(
                    title = "Dark Mode",
                    description = "Switch between light and dark themes",
                    isEnabled = preferences?.isDarkMode ?: false,
                    onToggle = { preferencesViewModel.toggleDarkMode() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingRow(
                    title = "Weather Sensitive",
                    description = "AI considers weather in recommendations",
                    isEnabled = preferences?.weatherSensitive ?: true,
                    onToggle = { preferencesViewModel.toggleWeatherSensitive() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingRow(
                    title = "Notifications",
                    description = "Daily outfit suggestions",
                    isEnabled = preferences?.notificationsEnabled ?: true,
                    onToggle = { preferencesViewModel.toggleNotifications() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                SettingRowWithArrow(
                    title = "Privacy Settings",
                    description = "Control your data and privacy",
                    isEnabled = preferences?.privacySettingsEnabled ?: false,
                    onToggle = { preferencesViewModel.togglePrivacySettings() }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
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
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable { onColorSelected(!isSelected) },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
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
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(12.dp)
            )
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
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Switch(
            checked = isEnabled,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
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
            .background(
                MaterialTheme.colorScheme.secondaryContainer,
                RoundedCornerShape(12.dp)
            )
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
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
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
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
            Icon(
                Icons.Filled.ArrowForward,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp)
            )
        }
    }
}
