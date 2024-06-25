package it.unibo.almamap.data

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface AlmaClass {
    @GET("res/campus.svg")
    suspend fun getCampusMap(): String

    @GET("res/floors/{floorCode}.svg")
    suspend fun getFloorMap(@Path("floorCode") code: String): String

    @GET("api/buildings")
    suspend fun getBuildings(): List<Building>

    @GET("api/spaces")
    suspend fun getSpaces(): List<Space>

    @GET("api/sensors/{sensorCode}")
    suspend fun getSensor(@Path("sensorCode") code: String): SensorData

    @GET("api/legend")
    suspend fun getLegend(): List<Legend>
}