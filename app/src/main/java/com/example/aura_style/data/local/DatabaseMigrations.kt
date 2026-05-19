package com.example.aura_style.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE wardrobe_items ADD COLUMN photoUri TEXT")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS favorites (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    outfitName TEXT NOT NULL,
                    occasion TEXT NOT NULL,
                    clothingItems TEXT NOT NULL,
                    savedDate TEXT NOT NULL,
                    isFavorited INTEGER NOT NULL DEFAULT 1
                )
            """)
        }
    }
}

