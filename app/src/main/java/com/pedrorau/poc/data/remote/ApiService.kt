package com.pedrorau.poc.data.remote

import com.pedrorau.poc.data.models.QuizRecord

interface ApiService {
    suspend fun getQuizData(): Result<QuizRecord>
}