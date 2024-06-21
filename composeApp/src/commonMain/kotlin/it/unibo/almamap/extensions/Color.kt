package it.unibo.almamap.extensions

import androidx.compose.ui.graphics.Color

fun Color.toHexCode(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return "#${red.toInt().toString(16)}${green.toInt().toString(16)}${blue.toInt().toString(16)}"
}

fun Color.toHexCodeWithAlpha(): String {
    val alpha = this.alpha*255
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return "#${alpha.toInt().toString(16)}${red.toInt().toString(16)}${green.toInt().toString(16)}${blue.toInt().toString(16)}"
}