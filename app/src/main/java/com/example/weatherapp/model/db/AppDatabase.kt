package com.example.weatherapp.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations

@Database(entities = [FavLocations::class , AlertDto::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
    abstract fun alartDao(): AlartDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fav_location_table"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
