package com.example.aura_style.data.di

import android.content.Context
import androidx.room.Room
import com.example.aura_style.data.local.AppDatabase
import com.example.aura_style.data.local.DatabaseMigrations
import com.example.aura_style.data.local.dao.FavoriteDao
import com.example.aura_style.data.local.dao.OutfitLogDao
import com.example.aura_style.data.local.dao.PreferencesDao
import com.example.aura_style.data.local.dao.UserDao
import com.example.aura_style.data.local.dao.WardrobeDao
import com.example.aura_style.data.repository.FavoriteRepository
import com.example.aura_style.data.repository.OutfitLogRepository
import com.example.aura_style.data.repository.PreferencesRepository
import com.example.aura_style.data.repository.UserRepository
import com.example.aura_style.data.repository.WardrobeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "aura_style_db"
        )
            .addMigrations(DatabaseMigrations.MIGRATION_2_3)
            .addMigrations(DatabaseMigrations.MIGRATION_3_4)
            .fallbackToDestructiveMigration(false)
            .build()

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    fun provideWardrobeDao(appDatabase: AppDatabase): WardrobeDao = appDatabase.wardrobeDao()

    @Provides
    fun providePreferencesDao(appDatabase: AppDatabase): PreferencesDao = appDatabase.preferencesDao()

    @Provides
    fun provideOutfitLogDao(appDatabase: AppDatabase): OutfitLogDao = appDatabase.outfitLogDao()

    @Provides
    fun provideFavoriteDao(appDatabase: AppDatabase): FavoriteDao = appDatabase.favoriteDao()

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepository(userDao)

    @Provides
    @Singleton
    fun provideWardrobeRepository(wardrobeDao: WardrobeDao): WardrobeRepository =
        WardrobeRepository(wardrobeDao)

    @Provides
    @Singleton
    fun providePreferencesRepository(preferencesDao: PreferencesDao): PreferencesRepository =
        PreferencesRepository(preferencesDao)

    @Provides
    @Singleton
    fun provideOutfitLogRepository(outfitLogDao: OutfitLogDao): OutfitLogRepository =
        OutfitLogRepository(outfitLogDao)

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository =
        FavoriteRepository(favoriteDao)
}
