package com.example.weatherapp.model.db
import com.example.weatherapp.model.dto.FavLocations

sealed class DbState {
    object Loading : DbState()
    data class Success(val data: List<FavLocations>) : DbState()
    data class Failure(val error: Throwable) : DbState()
}