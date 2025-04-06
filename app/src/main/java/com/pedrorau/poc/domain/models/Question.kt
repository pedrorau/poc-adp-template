package com.pedrorau.poc.domain.models

data class Question(
    val text: String,
    val answers: List<Answer>,
    val correctAnswerId: String,
    val correctAnswerText: String,
    val shuffleOptions: Boolean = false
)