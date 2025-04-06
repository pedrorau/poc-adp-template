package com.pedrorau.poc.presentation.droidHoot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedrorau.poc.domain.models.ConfigParameters
import com.pedrorau.poc.domain.models.Quiz
import com.pedrorau.poc.domain.useCases.GetConfigParametersUseCase
import com.pedrorau.poc.domain.useCases.GetQuizUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DroidHootViewModel(
    private val getQuizUseCase: GetQuizUseCase,
    private val getConfigParametersUseCase: GetConfigParametersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun loadQuiz() {
        viewModelScope.launch {

            val quizDeferred = async { getQuizUseCase() }
            val configDeferred = async { getConfigParametersUseCase() }

            val quizResult = quizDeferred.await()
            val configResult = configDeferred.await()

            if (quizResult.isSuccess && configResult.isSuccess) {
                val quiz = quizResult.getOrNull()!!
                val config = configResult.getOrNull()!!
                _uiState.value = QuizUiState.Success(quiz, config)
            } else {
                val errorMessage = when {
                    quizResult.isFailure -> quizResult.exceptionOrNull()?.message
                    configResult.isFailure -> configResult.exceptionOrNull()?.message
                    else -> "Unknown error"
                }
                _uiState.value = QuizUiState.Error(errorMessage ?: "Unknown error")
            }
        }
    }
}

sealed class QuizUiState {
    data object Loading: QuizUiState()
    data class Success(val quiz: Quiz, val configParameters: ConfigParameters) : QuizUiState()
    data class Error(val message: String) : QuizUiState()
}

