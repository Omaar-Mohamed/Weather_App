package com.example.weatherapp.model

import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo(
    private var alerts:MutableList<AlertDto>? = mutableListOf()
    , private var locations:MutableList<FavLocations>?
) : AppRepo {
    override suspend fun getWeather(
        lat: String,
        lon: String,
        apiKey: String,
        language: String,
        units: String
    ): Flow<WeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllLocations(): Flow<List<FavLocations>> {

        return flow {
            emit(locations ?: emptyList())
        }
    }

    override suspend fun insertLocation(location: FavLocations): Long {
        locations?.add(location)
        return location.id.toLong()
    }

    override suspend fun deleteLocation(location: FavLocations) {
        locations?.remove(location)
    }

    override suspend fun getAllAlarts(): Flow<List<AlertDto>> {
        return flow {
            emit(alerts ?: emptyList())
        }
    }

    override suspend fun insertAlart(alart: AlertDto): Long {
        alerts?.add(alart)
        return alart.id
    }

    override suspend fun deleteAlart(alart: AlertDto) {
        alerts?.remove(alart)
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