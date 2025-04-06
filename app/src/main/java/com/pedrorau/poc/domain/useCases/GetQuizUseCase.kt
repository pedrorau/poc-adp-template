package com.pedrorau.poc.domain.useCases

import com.pedrorau.poc.domain.models.Quiz
import com.pedrorau.poc.domain.repository.QuizRepository

class GetQuizUseCase(private val repository: QuizRepository) {
    suspend operator fun invoke(): Result<Quiz> {
        return repository.getQuiz()
    }
}