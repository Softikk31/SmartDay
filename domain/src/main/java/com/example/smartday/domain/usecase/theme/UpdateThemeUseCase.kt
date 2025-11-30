package com.example.smartday.domain.usecase.theme

import com.example.smartday.domain.repository.ThemeRepository

class UpdateThemeUseCase(private val repository: ThemeRepository) {
    suspend operator fun invoke(isDark: Boolean) = repository.updateThemeMode(isDark = isDark)
}