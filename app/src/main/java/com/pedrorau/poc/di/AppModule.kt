package com.pedrorau.poc.di

import com.pedrorau.poc.data.remote.ApiService
import com.pedrorau.poc.data.remote.ApiServiceImpl
import com.pedrorau.poc.data.repository.QuizRepositoryImpl
import com.pedrorau.poc.domain.repository.QuizRepository
import com.pedrorau.poc.domain.useCases.GetConfigParametersUseCase
import com.pedrorau.poc.domain.useCases.GetQuizUseCase
import org.koin.dsl.module

object AppModule {

    private val httpClient = NetworkModule.provideHttpClient()
    private val apiService: ApiService = ApiServiceImpl(httpClient)

    private val quizRepository: QuizRepository = QuizRepositoryImpl(apiService)

    val getQuizUseCase = GetQuizUseCase(quizRepository)
    val getConfigParametersUseCase = GetConfigParametersUseCase(quizRepository)
}

val appModule = module{
    // Nothing noew
}