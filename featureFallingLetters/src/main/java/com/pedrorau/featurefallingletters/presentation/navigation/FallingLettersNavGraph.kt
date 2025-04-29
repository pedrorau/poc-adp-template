package com.pedrorau.featurefallingletters.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pedrorau.featurefallingletters.presentation.main.FallingLettersScreen
import com.pedrorau.navigation.NavRegistry

//Register new routes
fun registerFallingLettersNavigation() {
    NavRegistry.register(FallingLettersDestinations.FALLING_LETTERS_MAIN) { navController ->
        featureNavGraph(navController)
    }
}

// Internal graph definition
fun NavGraphBuilder.featureNavGraph(navController: NavController) {
    composable(FallingLettersDestinations.FALLING_LETTERS_MAIN) {
        FallingLettersScreen (
            onBackPressed = {
                navController.popBackStack()
            }
        )
    }
}