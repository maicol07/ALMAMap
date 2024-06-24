package it.unibo.almamap

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

enum class PlatformType {
    ANDROID,
    IOS,
    WEB,
    DESKTOP
}

interface Platform {
    val type: PlatformType
    val name: String
    val version: Double
}

expect val platform: Platform

expect fun setDeviceLanguage(language: String)
expect fun getAppLanguage(): Flow<String>

expect fun openAppLanguageSettings()

@Composable
expect fun BackGestureHandler(
    onBack: () -> Unit
)