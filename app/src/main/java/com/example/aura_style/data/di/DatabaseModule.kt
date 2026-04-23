package com.example.aura_style.data.di

import android.content.Context
import com.example.aura_style.data.local.AppDatabase
import com.example.aura_style.data.local.dao.OutfitLogDao
import com.example.aura_style.data.local.dao.PreferencesDao
import com.example.aura_style.data.local.dao.UserDao
import com.example.aura_style.data.local.dao.WardrobeDao
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
        AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    fun provideWardrobeDao(appDatabase: AppDatabase): WardrobeDao = appDatabase.wardrobeDao()

    @Provides
    fun providePreferencesDao(appDatabase: AppDatabase): PreferencesDao = appDatabase.preferencesDao()

    @Provides
    fun provideOutfitLogDao(appDatabase: AppDatabase): OutfitLogDao = appDatabase.outfitLogDao()
}

