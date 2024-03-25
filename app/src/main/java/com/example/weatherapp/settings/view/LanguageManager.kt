package com.example.weatherapp.settings.view

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object LanguageManager {
    private val _languageFlow = MutableSharedFlow<Language>()
    val languageFlow: SharedFlow<Language> = _languageFlow

    var currentLanguage: Language = Language.ENGLISH
        private set(value) {
            field = value
            _languageFlow.tryEmit(value)
        }

    fun setLanguage(language: Language) {
        currentLanguage = language
    }
}

enum class Language {
    ENGLISH,
    ARABIC
}
