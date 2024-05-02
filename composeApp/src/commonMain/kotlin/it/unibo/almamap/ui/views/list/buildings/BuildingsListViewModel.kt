package it.unibo.almamap.ui.views.list.buildings

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
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BuildingsListViewModel: ViewModel(), KoinComponent {
    private val httpClient: HttpClient by inject()
    val buildings: SnapshotStateList<Building> = mutableStateListOf()
    var loading by mutableStateOf(true)

    init {
        loadBuildings()
    }

    fun loadBuildings() = viewModelScope.launch {
        loading = true
        buildings.clear()
        val response = httpClient.get("api/buildings")
        buildings.addAll(response.body<List<Building>>())
        loading = false
    }
}