package com.example.weatherapp.model.db

import android.content.Context
import com.example.weatherapp.model.dto.FavLocations
import kotlinx.coroutines.flow.Flow

class AppLocalDataSourseImpL private constructor(private val context: Context): AppLocalDataSourse {
    companion object {
        @Volatile
        private var instance: AppLocalDataSourseImpL? = null

        fun getInstance(context: Context): AppLocalDataSourseImpL {
            return instance ?: synchronized(this) {
                instance ?: AppLocalDataSourseImpL(context).also { instance = it }
            }
        }
    }

    override suspend fun insertLocation(location: FavLocations): Long {
        var id: Long = 0
        val appDao = AppDatabase.getInstance(context).appDao()
        id = appDao.insert(location)
        return id
    }

    override suspend fun deleteLocation(location: FavLocations) {
        val appDao = AppDatabase.getInstance(context).appDao()
        appDao.delete(location)
    }

    override suspend fun getLocationsFromDb(): Flow<List<FavLocations>> {
        val appDao = AppDatabase.getInstance(context).appDao()
        return appDao.getAll()
    }
}