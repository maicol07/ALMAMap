package it.unibo.almamap

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

expect fun setDeviceLanguage(language: String): Unit