package com.pedrorau.poc.domain.useCases

import com.pedrorau.poc.domain.models.ConfigParameters
import com.pedrorau.poc.domain.repository.QuizRepository

class GetConfigParametersUseCase(private val repository: QuizRepository) {

    suspend operator fun invoke(): Result<ConfigParameters> {
        return repository.getConfigParameters()
    }
}