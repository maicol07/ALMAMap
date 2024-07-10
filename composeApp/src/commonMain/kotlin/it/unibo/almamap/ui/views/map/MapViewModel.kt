package it.unibo.almamap.ui.views.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multiplatform.webview.web.WebViewNavigator
import it.unibo.almamap.data.AlmaClass
import it.unibo.almamap.data.Building
import it.unibo.almamap.data.BuildingFloor
import it.unibo.almamap.data.Legend
import it.unibo.almamap.data.Space
import it.unibo.almamap.ui.views.list.buildings.BuildingsListViewModel
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapViewModel : ViewModel(), KoinComponent {
    enum class Phase {
        Campus,
        Floor;

        fun previous(): Phase? = when (this) {
            Campus -> null
            Floor -> Campus
        }
    }

    private val api: AlmaClass by inject()
    private val buildingsListViewModel: BuildingsListViewModel by inject()
    val spacesListViewModel: SpacesListViewModel by inject()
    var loading by mutableStateOf(false)
    private var campusMap by mutableStateOf("")
    var phase by mutableStateOf<Phase?>(null)
    private var floorsMaps = mutableStateMapOf<BuildingFloor, String>()
    var selectedBuilding by mutableStateOf<Building?>(null)
    var selectedFloor by mutableStateOf<BuildingFloor?>(null)
    var selectedSpace by mutableStateOf<Space?>(null)
    var mapReady by mutableStateOf(false)
    var spacesListSheetOpened by mutableStateOf(false)
    var infoDialogOpened by mutableStateOf(false)
    var isLegendLoading by mutableStateOf(false)
    var legend = mutableStateListOf<Legend>()

    private val currentMaps: List<String>
        get(): List<String> = when (phase) {
            Phase.Campus -> listOf(campusMap)
            Phase.Floor -> listOfNotNull(floorsMaps[selectedBuilding?.floors?.find { it == selectedFloor }!!])
            null -> emptyList()
        }

    init {
        // Trigger init blocks
        buildingsListViewModel
        spacesListViewModel

        viewModelScope.launch {
            getLegend()
            getCampusMap()
            phase = Phase.Campus
        }
    }

    private suspend fun getLegend() {
        isLegendLoading = true
        legend.addAll(api.getLegend())
        isLegendLoading = false
    }

    private suspend fun getCampusMap() {
        loading = true
        campusMap = api.getCampusMap()
        loading = false
    }

    fun onMapClick(code: String) {
        if (loading || !mapReady) return
        println("Clicked on $code")
        when (phase) {
            Phase.Campus -> {
                val building = buildingsListViewModel.buildings.find { it.code == code }
                if (building != null) {
                    selectedBuilding = building
                    loading = true
                    println("Selected building: $selectedBuilding")
                    selectedFloor = null
                    viewModelScope.launch {
                        floorsMaps.putAll(
                            selectedBuilding?.floors?.map {
                                it to api.getFloorMap(
                                    it.svg.replace("/res/floors/", "").replace(".svg", "")
                                )
                            } ?: emptyList()
                        )
                        selectedFloor = selectedBuilding?.floors?.first()
                        phase = Phase.Floor
                        loading = false
                    }
                }
            }

            Phase.Floor -> {
                val space = spacesListViewModel.spaces.find { "${it.legend.name}_${it.code}" == code }
                println("Selected space: $space")
                if (space != null) {
                    selectedSpace = space
                }
            }

            null -> {}
        }
    }

    fun onListFabClick() {
        spacesListSheetOpened = true
    }

    fun drawMap(navigator: WebViewNavigator) = viewModelScope.launch {
        println("Selected building: $selectedBuilding, selected floor: $selectedFloor")
        println("Current maps: ${currentMaps.size}")

        // Wait until map is ready
        while (!mapReady) {
            navigator.evaluateJavaScript("window.kmpJsBridge !== undefined") {
                mapReady = it.toBoolean()
            }
            println("Waiting for map to be ready")
            delay(1000)
        }

        navigator.evaluateJavaScript("loadHTML(`${currentMaps.joinToString("")}`)")

        // Add markers
        navigator.evaluateJavaScript("clearMarkers()")
        when (phase) {
            Phase.Campus -> {
                val d = "M18 15h-2v2h2m0-6h-2v2h2m2 6h-8v-2h2v-2h-2v-2h2v-2h-2V9h8M10 7H8V5h2m0 6H8V9h2m0 6H8v-2h2m0 6H8v-2h2M6 7H4V5h2m0 6H4V9h2m0 6H4v-2h2m0 6H4v-2h2m6-10V3H2v18h20V7H12Z"
                for (building in buildingsListViewModel.buildings) {
                    navigator.evaluateJavaScript("addMarkerTo('${building.code}', '$d')")
                }
            }
            Phase.Floor -> {
                for (space in spacesListViewModel.filterSpaces(selectedBuilding!!, selectedFloor!!)) {
                    navigator.evaluateJavaScript("addMarkerTo('${space.legend.name}_${space.code.replace(".", "\\\\.")}', '${space.legend.svgd}')")
                }
            }
            else -> {}
        }
    }

    fun onBack() {
        if (phase != Phase.Campus) {
            phase = phase!!.previous()
        }
        // Check new phase
        if (phase != null) {
            selectedBuilding = null
            selectedFloor = null
        }
    }

    fun onInfoClick() {
        infoDialogOpened = true
    }
}