package it.unibo.almamap.data

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
)

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