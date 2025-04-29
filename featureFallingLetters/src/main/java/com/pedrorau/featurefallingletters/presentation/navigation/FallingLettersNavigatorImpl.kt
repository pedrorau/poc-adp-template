package com.pedrorau.featurefallingletters.presentation.navigation

import android.content.Context
import android.content.Intent
import com.pedrorau.core.navigation.FeatureNavigator
import com.pedrorau.featurefallingletters.presentation.main.FallingLettersActivity

class FallingLettersNavigatorImpl : FeatureNavigator {

    override fun navigateToFeature(context: Context, param1: String?, param2: Int?) {
        val intent = Intent(context, FallingLettersActivity::class.java).apply {
            param1?.let { putExtra("PARAM1", it) }
            param2?.let { putExtra("PARAM2", it) }
        }
        context.startActivity(intent)
    }
}