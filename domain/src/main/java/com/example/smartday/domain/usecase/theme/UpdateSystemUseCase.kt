package com.example.smartday.domain.usecase.theme

import com.example.smartday.domain.repository.ThemeRepository

class UpdateSystemUseCase(private val repository: ThemeRepository) {
    suspend operator fun invoke(isSystem: Boolean) = repository.updateSystemTheme(isSystem = isSystem)
}