package it.unibo.almamap.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import it.unibo.almamap.ui.components.spaces.SpaceListItem
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalFoundationApi::class)
@Composable
fun TopAppBarSearch(viewModel: SpacesListViewModel = koinViewModel<SpacesListViewModel>()) {
    var active by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val filteredSpaces = remember(viewModel.spaces, query) {
        if (query.isEmpty()) viewModel.spaces else
            viewModel.spaces.filter {
                val spaceBuilding =
                    viewModel.buildings.find { building -> building.id == it.floor?.buildingId }
                val spaceFloor = spaceBuilding?.floors?.find { floor -> floor.id == it.floor?.id }
                it.name.contains(query, ignoreCase = true) ||
                        spaceBuilding?.name?.contains(query, ignoreCase = true) ?: false ||
                        spaceFloor?.name?.contains(query, ignoreCase = true) ?: false
            }
    }

    AppBarSearchBar(
        active = active,
        onActiveChange = { active = it },
        query = query,
        onQueryChange = { query = it }) {
        LazyColumn {
            items(filteredSpaces, key = { it.id }) { space ->
                SpaceListItem(
                    space = space,
                    viewModel = viewModel,
                    modifier = Modifier.animateItemPlacement().animateContentSize()
                )
            }
        }
    }

    if (viewModel.selectedSpace != null) {
        val spaceBuilding =
            remember(viewModel.selectedSpace) { viewModel.buildings.find { building -> building.id == viewModel.selectedSpace!!.floor?.buildingId } }
        val spaceFloor =
            remember(viewModel.selectedSpace) { spaceBuilding?.floors?.find { floor -> floor.id == viewModel.selectedSpace!!.floor?.id } }
        SpaceBottomSheet(viewModel.selectedSpace!!, spaceBuilding!!, viewModel, spaceFloor!!) { viewModel.selectedSpace = null }
    }
}