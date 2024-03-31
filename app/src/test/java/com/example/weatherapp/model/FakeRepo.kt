package com.example.weatherapp.model

import com.example.weatherapp.model.db.AppLocalDataSourse
import com.example.weatherapp.model.dto.AlertDto
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.AppRemoteDataSourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRepo(
    private var fakeLocalDataSourse:AppLocalDataSourse,
    private var fakeRemoteDataSourse: AppRemoteDataSourse
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
            fakeLocalDataSourse.getLocationsFromDb().collect {
                emit(it)
            }
        }
    }

    override suspend fun insertLocation(location: FavLocations): Long {
        return fakeLocalDataSourse.insertLocation(location)
    }

    override suspend fun deleteLocation(location: FavLocations) {
        fakeLocalDataSourse.deleteLocation(location)
    }

    override suspend fun getAllAlarts(): Flow<List<AlertDto>> {
        return flow {
            fakeLocalDataSourse.getAlartsFromDb().collect {
                emit(it)
            }
        }
    }

    override suspend fun insertAlart(alart: AlertDto): Long {
        return fakeLocalDataSourse.insertAlart(alart)
    }

    override suspend fun deleteAlart(alart: AlertDto) {
        fakeLocalDataSourse.deleteAlart(alart)
    }

    override suspend fun getLastInsertedRow(): Flow<AlertDto?> {
        return flow {
            fakeLocalDataSourse.getLastInsertedRow().collect {
                emit(it)
            }
        }
    }

    override suspend fun deleteAlartById(alertId: Long) {
        fakeLocalDataSourse.deleteAlartById(alertId)
    }
}