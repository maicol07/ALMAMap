package it.unibo.almamap.ui.views.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.russhwolf.settings.ObservableSettings
import it.unibo.almamap.AvailableSettings
import it.unibo.almamap.ThemeOptions
import it.unibo.almamap.setDeviceLanguage
import it.unibo.almamap.utils.mutableStateOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.stopKoin

class SettingsViewModel: ViewModel(), KoinComponent {
    private val settings: ObservableSettings by inject()
    var useDeviceColors by settings.mutableStateOf(AvailableSettings.USE_DEVICE_COLORS.name, false)
    var theme by settings.mutableStateOf(AvailableSettings.THEME.name, ThemeOptions.FOLLOW_SYSTEM.name)
    var language by settings.mutableStateOf(AvailableSettings.LANGUAGE.name, "EN")

    fun changeLanguage(language: String) {
        this.language = language
        stopKoin()
        setDeviceLanguage(language)
    }
}