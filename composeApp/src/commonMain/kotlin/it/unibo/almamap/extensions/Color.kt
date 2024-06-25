package it.unibo.almamap.extensions

import androidx.annotation.ColorInt
import androidx.annotation.Size
import androidx.compose.ui.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

fun Color.toHexCode(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return "#${red.toInt().toString(16)}${green.toInt().toString(16)}${blue.toInt().toString(16)}"
}

fun Color.toHexCodeWithAlpha(): String {
    val alpha = this.alpha * 255
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return "#${alpha.toInt().toString(16)}${red.toInt().toString(16)}${
        green.toInt().toString(16)
    }${blue.toInt().toString(16)}"
}

@ColorInt
fun Color.Companion.parseColor(@Size(min = 1) colorString: String): Color {
    if (colorString[0] == '#') { // Use a long to avoid rollovers on #ffXXXXXX
        var color = colorString.substring(1).toLong(16)
        if (colorString.length == 7) { // Set the alpha value
            color = color or -0x1000000
        } else require(colorString.length == 9) { "Unknown color" }
        return Color(color.toInt())
    }
    throw IllegalArgumentException("Unknown color")
}


object ColorSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString(value.toHexCodeWithAlpha())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color.parseColor(decoder.decodeString())
    }
}
