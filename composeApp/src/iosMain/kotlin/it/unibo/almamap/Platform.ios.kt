package it.unibo.almamap

import platform.Foundation.NSUserDefaults
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