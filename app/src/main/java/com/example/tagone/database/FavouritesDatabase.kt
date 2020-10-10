package com.example.tagone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PostDatabaseFormat::class], version = 1)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract val favouritesDao: FavouritesDao
}

private lateinit var INSTANCE: FavouritesDatabase

fun getDatabase(context: Context): FavouritesDatabase {
    synchronized(FavouritesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                FavouritesDatabase::class.java,
                "favouritesDatabase"
            ).build()
        }
    }
    return INSTANCE
}