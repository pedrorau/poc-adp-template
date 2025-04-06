package com.pedrorau.poc.domain.repository

import com.pedrorau.poc.domain.models.ConfigParameters
import com.pedrorau.poc.domain.models.Quiz

interface QuizRepository {
    suspend fun getQuiz(): Result<Quiz>
    suspend fun getConfigParameters(): Result<ConfigParameters>
}