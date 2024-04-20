package it.unibo.almamap

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass

actual val platform: Platform = object : Platform {
    override val type: PlatformType = PlatformType.ANDROID
    override val name: String = Build.VERSION.RELEASE
    override val version: Double = Build.VERSION.SDK_INT.toDouble()
}

actual fun setDeviceLanguage(language: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
}