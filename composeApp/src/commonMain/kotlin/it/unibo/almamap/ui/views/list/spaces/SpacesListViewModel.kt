package it.unibo.almamap.ui.views.list.spaces

import androidx.compose.material3.ListItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import it.unibo.almamap.data.Building
import it.unibo.almamap.data.BuildingFloor
import it.unibo.almamap.data.Legend
import it.unibo.almamap.data.SensorData
import it.unibo.almamap.data.Space
import it.unibo.almamap.data.SpaceFloor
import it.unibo.almamap.ui.theme.AppListItemColors
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SpacesListViewModel: ViewModel(), KoinComponent {
    private val httpClient: HttpClient by inject()
    val buildings: SnapshotStateList<Building> = mutableStateListOf()
    val spaces: SnapshotStateList<Space> = mutableStateListOf()
    var loading by mutableStateOf(true)

    var selectedBuilding by mutableStateOf<Building?>(null)
    var selectedFloor by mutableStateOf<BuildingFloor?>(null)
    var selectedSpace by mutableStateOf<Space?>(null)

    val legendColors = mutableMapOf<Legend, AppListItemColors>()

    init {
        loadBuildings()
        loadSpaces()
    }

    fun loadSpaces() = viewModelScope.launch {
        loading = true
        spaces.clear()
        val response = httpClient.get("api/spaces")
        spaces.addAll(response.body<List<Space>>().filterNot { it.legend.code in listOf("buildings", "kiosks") })

        // Calculate colors for each legend
        var colors = AppListItemColors.entries.iterator()
        val legends = spaces.map { it.legend }.distinct()
        legendColors.putAll(legends.mapIndexed { index, legend ->
            if (!colors.hasNext()) colors = AppListItemColors.entries.iterator()
            legend to colors.next()
        })
        println("Legend colors: $legendColors")
        loading = false
    }

    suspend fun loadSensorData(code: String): SensorData {
        val response = httpClient.get("api/sensors/$code")
        return response.body<SensorData>()
    }

    fun loadBuildings() = viewModelScope.launch {
        loading = true
        buildings.clear()
        val response = httpClient.get("api/buildings")
        buildings.addAll(response.body<List<Building>>())
        loading = false
    }

    fun filterSpaces(building: Building, floor: BuildingFloor? = null) = spaces.filter {
        it.floor?.buildingId == building.id && (floor == null || it.floor.id == floor.id)
    }

    val visibleSpacesByType
        get() = (selectedBuilding?.let { filterSpaces(selectedBuilding!!, selectedFloor) } ?: spaces).map {
            it.legend to it
        }.groupBy({ it.first }, { it.second })
}