package com.pedrorau.poc.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QuizRecord(
    val record: QuizData
)

@Serializable
data class QuizData(
    val data: QuizResponse
)

@Serializable
data class QuizResponse(
    val configParameters: ConfigParameters,
    val questionsEN: List<QuestionInfo>,
    val questionsES: List<QuestionInfo>
) {
    @Serializable
    data class ConfigParameters(
        val timeLimit: Int,
        val totalQuestions: Int,
        val shuffleQuestions: Boolean
    )

    @Serializable
    data class QuestionInfo(
        val question: String,
        val answers: List<AnswerInfo>,
        val correctAnswerId: String,
        val correctAnswerText: String,
        val shuffleOptions: Boolean
    )

    @Serializable
    data class AnswerInfo(
        val id: String,
        val text: String
    )
}
