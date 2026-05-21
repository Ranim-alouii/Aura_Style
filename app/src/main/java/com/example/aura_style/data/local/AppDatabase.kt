package com.example.aura_style.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aura_style.data.local.dao.FavoriteDao
import com.example.aura_style.data.local.dao.OutfitLogDao
import com.example.aura_style.data.local.dao.PreferencesDao
import com.example.aura_style.data.local.dao.UserDao
import com.example.aura_style.data.local.dao.WardrobeDao
import com.example.aura_style.data.local.entity.Favorite
import com.example.aura_style.data.local.entity.OutfitLog
import com.example.aura_style.data.local.entity.Preferences
import com.example.aura_style.data.local.entity.User
import com.example.aura_style.data.local.entity.WardrobeItem

@Database(
    entities = [User::class, WardrobeItem::class, Preferences::class, OutfitLog::class, Favorite::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wardrobeDao(): WardrobeDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun outfitLogDao(): OutfitLogDao
    abstract fun favoriteDao(): FavoriteDao
}
