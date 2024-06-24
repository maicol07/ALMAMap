package it.unibo.almamap

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

actual val platform: Platform = object : Platform {
    override val type: PlatformType = PlatformType.ANDROID
    override val name: String = Build.VERSION.RELEASE
    override val version: Double = Build.VERSION.SDK_INT.toDouble()
}

actual fun setDeviceLanguage(language: String) {
    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
actual fun openAppLanguageSettings() {
    val context = AppActivity.INSTANCE
    val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS)
    intent.data = Uri.fromParts("package", context.packageName, null)
    context.startActivity(intent)
}

actual fun getAppLanguage(): Flow<String> = flow {
    val languageTag = LocaleListCompat.getAdjustedDefault().toLanguageTags()
    emit(languageTag.ifEmpty { "en" }.split("-")[0].uppercase())
}

@Composable
actual fun BackGestureHandler(
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
}