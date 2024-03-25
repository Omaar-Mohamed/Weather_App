package com.example.weatherapp.model.db

import android.content.Context
import com.example.weatherapp.model.dto.AlertDto
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

    override suspend fun insertAlart(alart: AlertDto): Long {
       val alartDao = AppDatabase.getInstance(context).alartDao()
        return alartDao.insertAlart(alart)
    }

    override suspend fun deleteAlart(alart: AlertDto) {
        val alartDao = AppDatabase.getInstance(context).alartDao()
        alartDao.deleteAlart(alart)
    }

    override suspend fun getAlartsFromDb(): Flow<List<AlertDto>> {
        val alartDao = AppDatabase.getInstance(context).alartDao()
        return alartDao.getAllAlarts()
    }

    override suspend fun getLastInsertedRow(): Flow<AlertDto?> {
        val alartDao = AppDatabase.getInstance(context).alartDao()
        return alartDao.getLastInsertedRow()
    }

    override suspend fun deleteAlartById(alertId: Long) {
        val alartDao = AppDatabase.getInstance(context).alartDao()
        alartDao.deleteAlartById(alertId)
    }
}