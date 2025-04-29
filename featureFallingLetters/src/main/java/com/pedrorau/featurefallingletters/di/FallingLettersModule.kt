package com.pedrorau.featurefallingletters.di

import com.pedrorau.featurefallingletters.presentation.navigation.registerFallingLettersNavigation
import org.koin.dsl.module

val featureModule = module {
    single {
        registerFallingLettersNavigation()
    }
}