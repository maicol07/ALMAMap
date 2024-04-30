package it.unibo.almamap.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule = module {
    single { Settings() as ObservableSettings }
}

val appModules = listOf(appModule, httpModule)