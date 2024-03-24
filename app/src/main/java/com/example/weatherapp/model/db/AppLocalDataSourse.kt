package com.example.weatherapp.model.db

import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import kotlinx.coroutines.flow.Flow

interface AppLocalDataSourse {
    suspend fun insertLocation(location: FavLocations): Long
    suspend fun deleteLocation(location: FavLocations)
    suspend fun getLocationsFromDb(): Flow<List<FavLocations>>

    suspend fun insertAlart(alart: AlertDto): Long
    suspend fun deleteAlart(alart: AlertDto)
    suspend fun getAlartsFromDb(): Flow<List<AlertDto>>

    suspend fun getLastInsertedRow(): Flow<AlertDto?>

}