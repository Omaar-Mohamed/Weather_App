package com.example.weatherapp.model

import com.example.weatherapp.model.db.AppLocalDataSourse
import com.example.weatherapp.model.dto.FavLocations
import com.example.weatherapp.model.dto.WeatherResponse
import com.example.weatherapp.model.network.AppRemoteDataSourse
import kotlinx.coroutines.flow.Flow

class AppRepoImpl private constructor(private val appRemoteDataSourse: AppRemoteDataSourse , private val appLocalDataSourse: AppLocalDataSourse)
    : AppRepo {

        companion object {
            @Volatile
            private var instance: AppRepoImpl? = null

            fun getInstance(appRemoteDataSourse: AppRemoteDataSourse , appLocalDataSourse: AppLocalDataSourse): AppRepoImpl {
                return instance ?: synchronized(this) {
                    instance ?: AppRepoImpl(appRemoteDataSourse , appLocalDataSourse).also { instance = it }
                }
            }
        }

    override suspend fun getWeather(
        lat: String,
        lon: String,
        apiKey: String
    ): Flow<WeatherResponse> {
        return appRemoteDataSourse.getWeather(lat, lon, apiKey)
    }

    override suspend fun getAllLocations(): Flow<List<FavLocations>> {
        return appLocalDataSourse.getLocationsFromDb()
    }

    override suspend fun insertLocation(location: FavLocations): Long {
        return appLocalDataSourse.insertLocation(location)
    }

    override suspend fun deleteLocation(location: FavLocations) {
        appLocalDataSourse.deleteLocation(location)
    }
}