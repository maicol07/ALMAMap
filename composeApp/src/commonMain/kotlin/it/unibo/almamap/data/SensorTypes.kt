package it.unibo.almamap.data

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.sensor__carbon_monoxide
import almamap.composeapp.generated.resources.sensor__comfort
import almamap.composeapp.generated.resources.sensor__humidity
import almamap.composeapp.generated.resources.sensor__nitrogen_dioxide
import almamap.composeapp.generated.resources.sensor__ozone
import almamap.composeapp.generated.resources.sensor__pm10
import almamap.composeapp.generated.resources.sensor__pm25
import almamap.composeapp.generated.resources.sensor__sound
import almamap.composeapp.generated.resources.sensor__temperature
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.BlurOn
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Grain
import androidx.compose.material.icons.outlined.Hearing
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

enum class SensorTypes(val label: StringResource, val icon: ImageVector) {
    COMFORT(Res.string.sensor__comfort, Icons.Outlined.Chair),
    TEMPERATURE(Res.string.sensor__temperature, Icons.Outlined.Thermostat),
    HUMIDITY(Res.string.sensor__humidity, Icons.Outlined.WaterDrop),
    SOUND(Res.string.sensor__sound, Icons.Outlined.Hearing),
    PM2_5(Res.string.sensor__pm25, Icons.Outlined.Grain),
    PM10(Res.string.sensor__pm10, Icons.Outlined.BlurOn),
    CARBON_MONOXIDE(Res.string.sensor__carbon_monoxide, Icons.Outlined.Air),
    NITROGEN_DIOXIDE(Res.string.sensor__nitrogen_dioxide, Icons.Outlined.Air),
    OZONE(Res.string.sensor__ozone, Icons.Outlined.Air),
}