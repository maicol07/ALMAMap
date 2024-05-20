package it.unibo.almamap.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import it.unibo.almamap.ui.views.list.buildings.BuildingsListViewModel
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel
import it.unibo.almamap.ui.views.settings.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Settings() }
    single { get<Settings>() as ObservableSettings }
    viewModel { SettingsViewModel() }
    viewModel { BuildingsListViewModel() }
    viewModel { SpacesListViewModel() }
}

val appModules = listOf(appModule, httpModule)