package com.example.weatherapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.db.AlertDbState
import com.example.weatherapp.model.dto.AlertDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class AlertViewModel (private val repository: AppRepo) : ViewModel(){
    private val _alert:MutableStateFlow<AlertDbState> = MutableStateFlow(AlertDbState.Loading)
    val alert:StateFlow<AlertDbState> get() = _alert

    private val _lastAlertInserted:MutableStateFlow<AlertDbState> = MutableStateFlow(AlertDbState.Loading)
    val lastAlertInserted:StateFlow<AlertDbState> get() = _lastAlertInserted

    private val _lastIndex = MutableStateFlow<Long>(0)
    val lastIndex: StateFlow<Long> = _lastIndex

    fun getAllAlerts(){
        viewModelScope.launch{
            repository.getAllAlarts()
                .catch { e->
                    _alert.value = AlertDbState.Failure(e)
                }.collect(){
                    _alert.value = AlertDbState.Success(it)
                }
        }
    }

    suspend fun insertAlert(alert: AlertDto): Long{
//        viewModelScope.launch {
//            repository.insertAlart(alert)
//        }
        return repository.insertAlart(alert)

    }

    fun deleteAlert(alert: AlertDto){
        viewModelScope.launch {
            repository.deleteAlart(alert)
        }
    }

    fun getLastInsertedRow(){
        viewModelScope.launch{
            repository.getLastInsertedRow()
                .catch { e->
                    _lastAlertInserted.value = AlertDbState.Failure(e)
                }.collect(){
                    _lastAlertInserted.value = AlertDbState.LastInsertedRow(it)
                }
        }
    }

    fun deleteAlertById(alertId: Long){
        viewModelScope.launch {
            repository.deleteAlartById(alertId)
        }
    }

    fun loadAlerts() {
        viewModelScope.launch {
            try {
                repository.getAllAlarts().collect { result ->
                    if (result.isNotEmpty()) {
                        _alert.value = AlertDbState.Success(result)
                        _lastIndex.value = result.last().id
                    } else {
                        // Handle empty result list
                    }
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }




}