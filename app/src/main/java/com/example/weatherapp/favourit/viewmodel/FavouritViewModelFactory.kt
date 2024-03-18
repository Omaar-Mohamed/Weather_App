package com.example.weatherapp.favourit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.AppRepo

class FavouritViewModelFactory(private val repository: AppRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritViewModel::class.java)) {
            return FavouritViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}