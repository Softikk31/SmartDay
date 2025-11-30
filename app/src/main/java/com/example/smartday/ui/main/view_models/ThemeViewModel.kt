package com.example.smartday.ui.main.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.domain.usecase.theme.GetThemeUseCase
import com.example.smartday.domain.usecase.theme.UpdatePrimaryUseCase
import com.example.smartday.domain.usecase.theme.UpdateSystemUseCase
import com.example.smartday.domain.usecase.theme.UpdateThemeUseCase
import com.example.smartday.ui.models.states.ThemeState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val updateThemeUseCase: UpdateThemeUseCase,
    private val updateSystemUseCase: UpdateSystemUseCase,
    private val updatePrimaryUseCase: UpdatePrimaryUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ViewModel() {
    private val _theme = MutableStateFlow(ThemeState())
    val theme = _theme.asStateFlow()

    init {
        viewModelScope.launch {
            getThemeUseCase().distinctUntilChanged().collect {
                _theme.value = ThemeState(
                    primaryColor = it.primaryColor,
                    systemTheme = it.systemTheme,
                    isDarkMode = it.isDarkMode
                )
            }
        }
    }

    fun editTheme(isDark: Boolean) {
        viewModelScope.launch {
            updateThemeUseCase(isDark)
        }
    }

    fun editSystem(isSystem: Boolean) {
        viewModelScope.launch {
            updateSystemUseCase(isSystem)
        }
    }

    fun editPrimary(color: ThemePrimaryColors) {
        viewModelScope.launch {
            updatePrimaryUseCase(color)
        }
    }
}