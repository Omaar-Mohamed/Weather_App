package com.example.weatherapp.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _languageFlow = MutableSharedFlow<String>(replay = 1)
    val languageFlow: SharedFlow<String> = _languageFlow

    private val _degreeFlow = MutableSharedFlow<String>(replay = 1)
    val degreeFlow: SharedFlow<String> = _degreeFlow


    fun setLanguage(language: String) {
        viewModelScope.launch {
            _languageFlow.emit(language)

        }
    }
    fun setDegree(degree: String) {
        viewModelScope.launch {
            _degreeFlow.emit(degree)

        }
    }

}