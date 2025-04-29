package com.pedrorau.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

typealias RegisterNavGraph = NavGraphBuilder.(NavController) -> Unit

// Navigation records
object NavRegistry {
    private val navRegistrations = mutableMapOf<String, RegisterNavGraph>()

    fun register(featureRoute: String, builder: RegisterNavGraph) {
        navRegistrations[featureRoute] = builder
    }

    fun getRegistration(featureRoute: String): RegisterNavGraph? {
        return navRegistrations[featureRoute]
    }

    fun getAllRegistrations(): Map<String, RegisterNavGraph> {
        return navRegistrations.toMap()
    }
}

// Extension for NavGraphBuilder that adds all registered graphs
fun NavGraphBuilder.appNavGraph(navController: NavController) {
    NavRegistry.getAllRegistrations().forEach { (_, register) ->
        register(this, navController)
    }
}
