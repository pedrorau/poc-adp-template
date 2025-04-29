package com.pedrorau.poc

import android.app.Application
import com.pedrorau.featurefallingletters.di.featureModule
import com.pedrorau.poc.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PocApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Referencia al contexto de la aplicación
            androidContext(this@PocApplication)

            // Lista de todos los módulos de la aplicación
            modules(listOf(
                // Si tienes un módulo para la app principal
                appModule,

                // El módulo del feature que acabas de crear
                featureModule

                // Otros módulos...
            ))
        }
    }
}