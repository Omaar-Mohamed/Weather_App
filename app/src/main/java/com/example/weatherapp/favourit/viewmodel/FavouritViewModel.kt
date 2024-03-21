package com.example.weatherapp.favourit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.AppRepo
import com.example.weatherapp.model.db.DbState
import com.example.weatherapp.model.dto.FavLocations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavouritViewModel(private val repostory:AppRepo) : ViewModel(){
    private val _allLocations:MutableStateFlow<DbState> = MutableStateFlow(DbState.Loading)
    val allLocations:StateFlow<DbState> get() = _allLocations
    fun getAllLocations(){
        viewModelScope.launch{
            repostory.getAllLocations()
                .catch { e->
                    _allLocations.value = DbState.Failure(e)
                }.collect(){
                    _allLocations.value = DbState.Success(it)
                }
        }
    }

    fun insertLocation(location: FavLocations){
        viewModelScope.launch {
            repostory.insertLocation(location)
        }


    }

    fun deleteLocation(location: FavLocations){
        viewModelScope.launch {
            repostory.deleteLocation(location)
        }
    }
}