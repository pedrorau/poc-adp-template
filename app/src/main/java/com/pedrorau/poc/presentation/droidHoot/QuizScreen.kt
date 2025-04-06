package com.pedrorau.poc.presentation.droidHoot

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pedrorau.poc.di.AppModule
import com.pedrorau.poc.domain.models.Quiz

@Composable
fun QuizScreen(
    onBackPressed: () -> Unit,
) {

    BackHandler {
        onBackPressed()
    }

    val viewModel = remember {
        DroidHootViewModel(
            AppModule.getQuizUseCase,
            AppModule.getConfigParametersUseCase
        )
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadQuiz()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF46178F)),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is QuizUiState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            is QuizUiState.Success -> {
                QuizContent(quiz = state.quiz)
            }
            is QuizUiState.Error -> {
                ErrorMessage(message = state.message)
            }
        }
    }
}

@Composable
fun QuizContent(quiz: Quiz) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = quiz.questionsEN[0].text,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = quiz.questionsEN[0].correctAnswerText,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Red)
    ) {
        Text(
            text = "Error: $message",
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}