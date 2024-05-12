package it.unibo.almamap.ui.views.list.spaces

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.list__all_buildings
import almamap.composeapp.generated.resources.list__all_floors
import almamap.composeapp.generated.resources.list__buildings_title
import almamap.composeapp.generated.resources.list__floors_title
import almamap.composeapp.generated.resources.list__no_spaces_found
import almamap.composeapp.generated.resources.loading
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Domain
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Sensors
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import it.unibo.almamap.App
import it.unibo.almamap.data.Legend
import it.unibo.almamap.data.Space
import it.unibo.almamap.ui.components.Select
import it.unibo.almamap.ui.theme.AppListItemColors
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun SpacesListView(
    viewModel: SpacesListViewModel = viewModel<SpacesListViewModel>()
) {
    AnimatedVisibility(viewModel.loading) {
        LazyColumn {
            items(5) {
                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(Res.string.loading),
                            Modifier.placeholder(true, highlight = PlaceholderHighlight.fade())
                                .fillMaxWidth()
                        )
                    },
                )
            }
        }
    }
    AnimatedVisibility(!viewModel.loading && viewModel.spaces.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Rounded.WarningAmber, null, Modifier.size(64.dp))
            Text(stringResource(Res.string.list__no_spaces_found))
        }
    }

    val openedCategories = remember { mutableStateListOf<Legend>() }

    AnimatedVisibility(!viewModel.loading && viewModel.spaces.isNotEmpty()) {
        LazyColumn {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val allBuildingsString = stringResource(Res.string.list__all_buildings)
                    Select(
                        modifier = Modifier.weight(1f),
                        selectedValue = viewModel.selectedBuilding?.name ?: allBuildingsString,
                        options = listOf(allBuildingsString) + viewModel.buildings.map { it.name },
                        label = stringResource(Res.string.list__buildings_title),
                        onValueChangedEvent = {
                            if (it == allBuildingsString) {
                                viewModel.selectedBuilding = null
                                return@Select
                            }
                            viewModel.selectedBuilding =
                                viewModel.buildings.find { building -> building.name == it }
                        }
                    )
                    val allFloorsString = stringResource(Res.string.list__all_floors)
                    Select(
                        modifier = Modifier.weight(1f),
                        selectedValue = viewModel.selectedFloor?.name ?: allFloorsString,
                        options = listOf(allFloorsString) + (viewModel.selectedBuilding?.floors?.map { it.name }
                            ?: emptyList()),
                        label = stringResource(Res.string.list__floors_title),
                        enabled = viewModel.selectedBuilding != null,
                        onValueChangedEvent = {
                            if (it == allFloorsString) {
                                viewModel.selectedFloor = null
                                return@Select
                            }
                            viewModel.selectedFloor =
                                viewModel.selectedBuilding!!.floors.find { floor -> floor.name == it }
                        }
                    )
                }
            }

            for ((legend, spaces) in viewModel.visibleSpacesByType.entries.sortedBy { it.key.priority }.filterNot { it.value.isEmpty() }) {
                item(key = legend.id) {
                    val expanded = remember(openedCategories.size) { openedCategories.contains(legend) }
                    ListItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .animateEnterExit()
                            .animateContentSize()
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.large)
                            .clickable {
                                if (expanded) {
                                    openedCategories.remove(legend)
                                } else {
                                    openedCategories.add(legend)
                                }
                            },
                        headlineContent = { Text(legend.name) },
                        trailingContent = {
                            Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, null)
                        },
                        colors = viewModel.legendColors[legend]!!.colors()
                    )
                }

                if (openedCategories.contains(legend)) {
                    items(spaces, { it.code }) { space ->
                        SpaceListItem(
                            space,
                            Modifier
                                .animateItemPlacement()
                                .animateEnterExit()
                                .animateContentSize(),
                            viewModel
                        )
                    }
                }
            }

        }
        if (viewModel.selectedSpace != null) {
            ModalBottomSheet(onDismissRequest = { viewModel.selectedSpace = null }) {
                Text(viewModel.selectedSpace!!.name)
                viewModel.selectedSpace!!.description?.let { Text(it) }
                Text("Posti: ${viewModel.selectedSpace!!.capacity ?: "N/A"}")
                Text(viewModel.selectedSpace!!.sensors.joinToString { it.code })
//                Text(viewModel.selectedSpace!!.floor?.number ?: "")
                Text(viewModel.selectedSpace!!.floor?.buildingId.toString())
//                Text(viewModel.selectedSpace!!.building?.name ?: "")
            }
        }
    }
}

@Composable
private fun SpaceListItem(space: Space, modifier: Modifier, viewModel: SpacesListViewModel) {
    val spaceBuilding =
        remember(space) { viewModel.buildings.find { building -> building.id == space.floor?.buildingId } }
    val spaceFloor =
        remember(space) { spaceBuilding?.floors?.find { floor -> floor.id == space.floor?.id } }
    val overline = remember {
        spaceFloor?.let {
            spaceBuilding?.let {
                "${spaceBuilding.name} - ${spaceFloor.name}"
            } ?: spaceFloor.name
        } ?: spaceBuilding?.name
    }
    ListItem(
        modifier = Modifier
            .clickable { viewModel.selectedSpace = space }
            .then(modifier),
        headlineContent = { Text(space.name) },
        supportingContent = space.description?.ifBlank { null }?.let { { Text(it) } },
        overlineContent = overline?.let { { Text(it) } },
        leadingContent = space.sensors.ifEmpty { null }?.let {
            { Icon(Icons.Rounded.Memory, null) }
        },
        trailingContent = { Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null) },
    )
}