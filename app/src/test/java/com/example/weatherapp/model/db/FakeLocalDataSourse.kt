package com.example.weatherapp.model.db

import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSourse(private var alerts:MutableList<AlertDto>? = mutableListOf()
                          , private var locations:MutableList<FavLocations>?) : AppLocalDataSourse{
    override suspend fun insertLocation(location: FavLocations): Long {
        locations?.add(location)
        return location.id.toLong()
    }

    override suspend fun deleteLocation(location: FavLocations) {
        locations?.remove(location)
    }

    override suspend fun getLocationsFromDb(): Flow<List<FavLocations>> {
        return flow {
            emit(locations ?: emptyList())
        }
    }

    override suspend fun insertAlart(alert: AlertDto): Long {
        alerts?.add(alert)
        return alert.id
    }

    override suspend fun deleteAlart(alert: AlertDto) {
        alerts?.remove(alert)
    }

    override suspend fun getAlartsFromDb(): Flow<List<AlertDto>> {
        return flow {
            emit(alerts ?: emptyList())
        }
    }

    override suspend fun getLastInsertedRow(): Flow<AlertDto?> {
        return flow {
            emit(alerts?.lastOrNull())
        }
    }

    override suspend fun deleteAlartById(alertId: Long) {
        alerts?.removeIf { it.id == alertId }
    }

}