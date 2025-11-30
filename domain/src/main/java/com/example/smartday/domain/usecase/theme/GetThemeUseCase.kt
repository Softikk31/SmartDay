package com.example.smartday.domain.usecase.theme

import com.example.smartday.core.models.theme.ThemeModel
import com.example.smartday.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(private val repository: ThemeRepository) {
    operator fun invoke() : Flow<ThemeModel> = repository.getTheme()
}