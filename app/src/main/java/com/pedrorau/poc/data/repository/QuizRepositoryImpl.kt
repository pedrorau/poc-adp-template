package com.pedrorau.poc.data.repository

import com.pedrorau.poc.data.models.QuizRecord
import com.pedrorau.poc.data.models.QuizResponse
import com.pedrorau.poc.data.remote.ApiService
import com.pedrorau.poc.domain.models.Answer
import com.pedrorau.poc.domain.models.ConfigParameters
import com.pedrorau.poc.domain.models.Question
import com.pedrorau.poc.domain.models.Quiz
import com.pedrorau.poc.domain.repository.QuizRepository

class QuizRepositoryImpl(private val apiService: ApiService): QuizRepository {

    override suspend fun getQuiz(): Result<Quiz> {
        return apiService.getQuizData().map { response ->
            mapToQuiz(response)
        }
    }

    override suspend fun getConfigParameters(): Result<ConfigParameters> {
        return apiService.getQuizData().map { response ->
            mapConfigParameters(response.record.data.configParameters)
        }
    }

    private fun mapToQuiz(response: QuizRecord): Quiz {
        return Quiz(
            questionsEN = response.record.data.questionsEN.map { question ->
                Question(
                    text = question.question,
                    answers = question.answers.map { answer ->
                        Answer(
                            id = answer.id,
                            text = answer.text
                        )
                    },
                    correctAnswerText = question.correctAnswerId,
                    correctAnswerId = question.correctAnswerId
                )
            },
            questionsES = response.record.data.questionsES.map { question ->
                Question(
                    text = question.question,
                    answers = question.answers.map { answer ->
                        Answer(
                            id = answer.id,
                            text = answer.text
                        )
                    },
                    correctAnswerText = question.correctAnswerId,
                    correctAnswerId = question.correctAnswerId
                )
            },
        )
    }

    private fun mapConfigParameters(parameters: QuizResponse.ConfigParameters): ConfigParameters {
        return ConfigParameters(
            timeLimit = parameters.timeLimit,
            totalQuestions = parameters.totalQuestions,
        )
    }
}
