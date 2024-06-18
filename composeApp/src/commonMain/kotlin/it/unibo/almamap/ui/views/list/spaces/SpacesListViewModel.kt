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
import it.unibo.almamap.data.AlmaClass
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
    private val api: AlmaClass by inject()
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
        spaces.addAll(api.getSpaces().filterNot { it.legend.code in listOf("buildings", "kiosks") })

        // Calculate colors for each legend
        var colors = AppListItemColors.entries.iterator()
        val legends = spaces.map { it.legend }.distinct()
        legendColors.putAll(legends.mapIndexed { index, legend ->
            if (!colors.hasNext()) colors = AppListItemColors.entries.iterator()
            legend to colors.next()
        })
        loading = false
    }

    suspend fun loadSensorData(code: String): SensorData {
        return api.getSensor(code)
    }

    fun loadBuildings() = viewModelScope.launch {
        loading = true
        buildings.clear()
        buildings.addAll(api.getBuildings())
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