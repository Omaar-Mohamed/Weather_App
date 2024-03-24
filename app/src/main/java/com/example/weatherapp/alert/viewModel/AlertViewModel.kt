package com.example.weatherapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.db.AlertDbState
import com.example.weatherapp.model.dto.AlertDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel (private val repository: AppRepo) : ViewModel(){
    private val _alert:MutableStateFlow<AlertDbState> = MutableStateFlow(AlertDbState.Loading)
    val alert:StateFlow<AlertDbState> get() = _alert

    private val _lastAlertInserted:MutableStateFlow<AlertDbState> = MutableStateFlow(AlertDbState.Loading)
    val lastAlertInserted:StateFlow<AlertDbState> get() = _lastAlertInserted

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

    fun insertAlert(alert: AlertDto){
        viewModelScope.launch {
            repository.insertAlart(alert)
        }
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
}