package com.example.weatherapp.Home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.model.AppRepo

class HomeViewModelFactory (private val repository: AppRepo , private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(repository , sharedPreferences) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}