package com.example.aura_style.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aura_style.data.local.dao.OutfitLogDao
import com.example.aura_style.data.local.dao.PreferencesDao
import com.example.aura_style.data.local.dao.UserDao
import com.example.aura_style.data.local.dao.WardrobeDao
import com.example.aura_style.data.local.entity.OutfitLog
import com.example.aura_style.data.local.entity.Preferences
import com.example.aura_style.data.local.entity.User
import com.example.aura_style.data.local.entity.WardrobeItem

@Database(
    entities = [User::class, WardrobeItem::class, Preferences::class, OutfitLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun wardrobeDao(): WardrobeDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun outfitLogDao(): OutfitLogDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aura_style_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

