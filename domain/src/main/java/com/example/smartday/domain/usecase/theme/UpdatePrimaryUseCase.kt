package com.example.smartday.domain.usecase.theme

import com.example.smartday.core.enums.ThemePrimaryColors
import com.example.smartday.domain.repository.ThemeRepository

class UpdatePrimaryUseCase(private val repository: ThemeRepository) {
    suspend operator fun invoke(color: ThemePrimaryColors) = repository.updatePrimaryTheme(color = color)
}