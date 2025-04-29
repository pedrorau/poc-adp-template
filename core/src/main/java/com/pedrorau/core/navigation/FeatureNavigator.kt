package com.pedrorau.core.navigation

import android.content.Context

interface FeatureNavigator {
    fun navigateToFeature(context: Context, param1: String? = null, param2: Int? = null)
}