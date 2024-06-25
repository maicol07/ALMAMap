package it.unibo.almamap.data

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.book_open_blank_variant
import almamap.composeapp.generated.resources.desktop_tower
import almamap.composeapp.generated.resources.elevator
import almamap.composeapp.generated.resources.human_male_female
import almamap.composeapp.generated.resources.stairs
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.School
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import it.unibo.almamap.extensions.ColorSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Building(
    val code: String,
    val description: String,
    val floors: List<BuildingFloor>,
    val id: Int,
    val legend: Legend,
    val name: String
)

@Serializable
data class BuildingFloor(
    val id: Int,
    val name: String,
    val number: Int,
    val svg: String
)

@Serializable
data class Legend(
    val code: String,
    val id: Int,
    val name: String,
    val nameEng: String,
    val priority: Int
) {
    val svgd = when (code) {
        "classrooms" -> "M12 3L1 9l11 6l9-4.91V17h2V9M5 13.18v4L12 21l7-3.82v-4L12 17l-7-3.82Z"
        "labs" -> "M8 2h8a2 2 0 0 1 2 2v16a2 2 0 0 1-2 2H8a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2m0 2v2h8V4H8m8 4H8v2h8V8m0 10h-2v2h2v-2Z"
        "offices" -> "M12 4a4 4 0 0 1 4 4a4 4 0 0 1-4 4a4 4 0 0 1-4-4a4 4 0 0 1 4-4m0 10c4.42 0 8 1.79 8 4v2H4v-2c0-2.21 3.58-4 8-4Z"
        "services" -> "M21 5c-1.11-.35-2.33-.5-3.5-.5c-1.95 0-4.05.4-5.5 1.5c-1.45-1.1-3.55-1.5-5.5-1.5c-1.95 0-4.05.4-5.5 1.5v14.65c0 .25.25.5.5.5c.1 0 .15-.05.25-.05C3.1 20.45 5.05 20 6.5 20c1.95 0 4.05.4 5.5 1.5c1.35-.85 3.8-1.5 5.5-1.5c1.65 0 3.35.3 4.75 1.05c.1.05.15.05.25.05c.25 0 .5-.25.5-.5V6c-.6-.45-1.25-.75-2-1m0 13.5c-1.1-.35-2.3-.5-3.5-.5c-1.7 0-4.15.65-5.5 1.5V8c1.35-.85 3.8-1.5 5.5-1.5c1.2 0 2.4.15 3.5.5v11.5Z"
        "toilette" -> "M7.5 2a2 2 0 0 1 2 2a2 2 0 0 1-2 2a2 2 0 0 1-2-2a2 2 0 0 1 2-2M6 7h3a2 2 0 0 1 2 2v5.5H9.5V22h-4v-7.5H4V9a2 2 0 0 1 2-2m10.5-5a2 2 0 0 1 2 2a2 2 0 0 1-2 2a2 2 0 0 1-2-2a2 2 0 0 1 2-2M15 22v-6h-3l2.59-7.59C14.84 7.59 15.6 7 16.5 7c.9 0 1.66.59 1.91 1.41L21 16h-3v6h-3Z"
        "stairs" -> "M15 5v4h-4v4H7v4H3v3h7v-4h4v-4h4V8h4V5h-7Z"
        "elevators" -> "m7 2l4 4H8v4H6V6H3l4-4m10 8l-4-4h3V2h2v4h3l-4 4M7 12h10a2 2 0 0 1 2 2v6a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2v-6a2 2 0 0 1 2-2m0 2v6h10v-6H7Z"
        else -> ""
    }

    @Serializable(ColorSerializer::class)
    val color = when (code) {
        "classrooms" -> Color.Red
        "labs" -> Color(255, 183, 77)
        "offices" -> Color(33, 150, 243)
        "services" -> Color(186, 104, 200)
        "toilette" -> Color(77, 208, 225)
        /*"stairs", "elevators", */else -> Color(199, 199, 201)
    }

    /**
     * The icon associated with the legend.
     * Can be a [ImageVector] or a [org.jetbrains.compose.resources.DrawableResource] representing a drawable resource.
     */
    @Contextual
    val icon: Any? = when (code) {
        "classrooms" -> Icons.Outlined.School
        "labs" -> Res.drawable.desktop_tower
        "offices" -> Icons.Outlined.Person
        "services" -> Res.drawable.book_open_blank_variant
        "toilette" -> Res.drawable.human_male_female
        "stairs" -> Res.drawable.stairs
        "elevators" -> Res.drawable.elevator
        else -> null
    }
}

@Serializable
data class Space(
    val capacity: Int?,
    val code: String,
    val description: String?,
//    val directionsFrom: List<Space>,
//    val directionsTo: List<Space>,
    val floor: SpaceFloor?,
    val id: Int,
    val legend: Legend,
    val name: String,
    val sensors: List<Sensor>
) {
    fun getBuilding(buildings: List<Building>): Building? {
        return buildings.find { it.id == floor?.buildingId }
    }

    fun getFloor(building: Building): BuildingFloor? {
        return building.floors.find { it.id == floor?.id }
    }

    fun getFloor(buildings: List<Building>): BuildingFloor? {
        return getBuilding(buildings)?.floors?.find { it.id == floor?.id }
    }
}

@Serializable
data class SpaceFloor(
    val buildingId: Int,
    val id: Int,
    val number: Int
)

@Serializable
data class Sensor(
    val code: String,
    val type: SensorType
)

@Serializable
data class SensorType(
    val code: String,
    val unit: SensorUnit
)

@Serializable
data class SensorUnit(
    val code: String,
    val symbol: String
)

@Serializable
data class SensorData(
    val dayOfWeekAverage: List<List<Double>>,
    val live: SensorLiveData
)

@Serializable
data class SensorLiveData(
    val index: Int,
    val scale: List<SensorScale>,
    val value: Float
)

@Serializable
data class SensorScale(
    val code: String,
    val index: Int,
    val label: String,
    val lowerBound: Int?,
    val upperBound: Int?
)