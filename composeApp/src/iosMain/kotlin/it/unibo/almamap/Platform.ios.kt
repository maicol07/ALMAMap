package it.unibo.almamap

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import platform.Foundation.NSURL
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

actual val platform: Platform = object : Platform {
    override val type: PlatformType = PlatformType.IOS
    override val name: String = UIDevice.currentDevice.systemName
    override val version: Double = UIDevice.currentDevice.systemVersion.toDouble()
}

actual fun setDeviceLanguage(language: String) {
    NSUserDefaults.standardUserDefaults().setObject(listOf(language), forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults().synchronize()
}

actual fun openAppLanguageSettings() {
    UIApplication.sharedApplication.openURL(NSURL.URLWithString("App-Prefs:root=General&path=LANGUAGE")!!)
}

actual fun getAppLanguage(): Flow<String> = flow {
    emit(NSUserDefaults.standardUserDefaults().objectForKey("AppleLanguages") as? String ?: "EN")
}

@Composable
actual fun BackGestureHandler(
    onBack: () -> Unit
) {
    // Not implemented
}