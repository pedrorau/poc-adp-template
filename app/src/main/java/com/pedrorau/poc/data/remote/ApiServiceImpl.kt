package com.pedrorau.poc.data.remote

import com.pedrorau.poc.data.models.QuizRecord
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class ApiServiceImpl(private val client: HttpClient): ApiService {

    override suspend fun getQuizData(): Result<QuizRecord> {
        return try {
            val response = client.get("https://api.jsonbin.io/v3/b/67f214e28561e97a50f989fa")
            if (response.status.isSuccess()) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}