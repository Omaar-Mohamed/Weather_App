package com.example.weatherapp.alert.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.AppRepo

class AlertViewModelFactory(private val repository: AppRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertViewModel::class.java)) {
            return AlertViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}