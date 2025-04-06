package com.pedrorau.poc.domain.models

data class ConfigParameters(
    val timeLimit: Int,
    val totalQuestions: Int,
    val shuffleQuestions: Boolean = false
)
