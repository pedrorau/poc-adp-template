package com.pedrorau.poc.presentation.navigation

// Registra las rutas principales de la app
fun registerAppNavigation() {
    NavRegistry.register(AppDestinations.HOME_ROUTE) { navController ->
        appNavGraph(navController)
    }
}

// Gráfico de navegación del módulo app
fun NavGraphBuilder.appNavGraph(navController: NavController) {
    composable(AppDestinations.HOME_ROUTE) {
        HomeScreen(
            navigateToFeature = {
                navController.navigate(AppDestinations.FEATURE_ENTRY)
            },
            navigateToSettings = {
                navController.navigate(AppDestinations.SETTINGS_ROUTE)
            }
        )
    }

    composable(AppDestinations.SETTINGS_ROUTE) {
        SettingsScreen(
            onBackClick = { navController.popBackStack() }
        )
    }
}