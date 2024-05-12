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
)

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