package com.example.weatherapp.model.db

import com.example.weatherapp.model.dto.AlertDto
import kotlinx.coroutines.flow.Flow

sealed class AlertDbState {
    object Loading : AlertDbState()
    data class Success(val data: List<AlertDto>) : AlertDbState()

    data class LastInserted(val data: Flow<List<AlertDto>>) : AlertDbState()

    data class LastInsertedRow(val data: AlertDto?) : AlertDbState()
    data class Failure(val error: Throwable) : AlertDbState()

}