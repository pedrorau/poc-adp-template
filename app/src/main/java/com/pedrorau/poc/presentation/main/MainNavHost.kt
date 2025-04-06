package com.pedrorau.poc.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.pedrorau.poc.presentation.droidHoot.DroidHootScreen
import com.pedrorau.poc.presentation.droidHoot.QuizScreen
import com.pedrorau.poc.presentation.fallingLetters.FallingLettersScreen

@Composable
fun MainNavHost(
    onClose: () -> Unit = { },
) {
    val navController = rememberNavController()

    val navGraph = navController.createGraph(
        startDestination = Screens.Main.route
    ) {
        composable(Screens.Main.route) {
            MainScreen(
                onBackPressed = {
                    onClose()
                },
                goToFallingLetters = {
                    navController.navigate(Screens.FallingLetters.route)
                },
                goToDroidHoot = {
                    navController.navigate(Screens.DroidHoot.route)
                }
            )
        }
        composable(Screens.FallingLetters.route) {
            FallingLettersScreen(
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
        composable(Screens.DroidHoot.route) {
            DroidHootScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                onJoinClick = {
                    navController.navigate(Screens.QuizScreen.route)
                }
            )
        }
        composable(Screens.QuizScreen.route) {
            QuizScreen(
                onBackPressed = {
                    navController.navigateUp()
                }
            )
        }
    }

    NavHost(
        navController = navController,
        graph = navGraph
    )
}

sealed class Screens(val route: String) {
    data object Main: Screens("initialScreen")
    data object FallingLetters: Screens("fallingLettersScreen")
    data object DroidHoot: Screens("droidHootScreen")
    data object QuizScreen: Screens("quizScreen")
}