package it.unibo.almamap.ui.views.list.spaces

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.list__all_buildings
import almamap.composeapp.generated.resources.list__all_floors
import almamap.composeapp.generated.resources.list__buildings_title
import almamap.composeapp.generated.resources.list__floors_title
import almamap.composeapp.generated.resources.list__no_spaces_found
import almamap.composeapp.generated.resources.loading
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import it.unibo.almamap.ui.components.FilledSelect
import it.unibo.almamap.ui.components.spaces.SpaceBottomSheet
import it.unibo.almamap.ui.components.spaces.SpaceListItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SpacesListView(
    viewModel: SpacesListViewModel = koinViewModel<SpacesListViewModel>()
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

    AnimatedVisibility(!viewModel.loading && viewModel.spaces.isNotEmpty()) {
        val expandedCategories = remember { mutableStateMapOf(*viewModel.spaces.map { it.legend.id to true }.toTypedArray()) }
        LazyColumn {
            item {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val allBuildingsString = stringResource(Res.string.list__all_buildings)
                    FilledSelect(
                        modifier = Modifier.weight(1f),
                        selectedValue = viewModel.selectedBuilding?.name ?: allBuildingsString,
                        options = listOf(allBuildingsString) + viewModel.buildings.map { it.name },
                        label = stringResource(Res.string.list__buildings_title),
                        onValueChangedEvent = {
                            if (it == allBuildingsString) {
                                viewModel.selectedBuilding = null
                                viewModel.selectedFloor = null
                                return@FilledSelect
                            }
                            viewModel.selectedBuilding =
                                viewModel.buildings.find { building -> building.name == it }
                        }
                    )
                    val allFloorsString = stringResource(Res.string.list__all_floors)
                    FilledSelect(
                        modifier = Modifier.weight(1f),
                        selectedValue = viewModel.selectedFloor?.name ?: allFloorsString,
                        options = listOf(allFloorsString) + (viewModel.selectedBuilding?.floors?.map { it.name }
                            ?: emptyList()),
                        label = stringResource(Res.string.list__floors_title),
                        enabled = viewModel.selectedBuilding != null,
                        onValueChangedEvent = {
                            if (it == allFloorsString) {
                                viewModel.selectedFloor = null
                                return@FilledSelect
                            }
                            viewModel.selectedFloor =
                                viewModel.selectedBuilding!!.floors.find { floor -> floor.name == it }
                        }
                    )
                }
            }

            for ((legend, spaces) in viewModel.visibleSpacesByType.entries.sortedBy { it.key.priority }.filterNot { it.value.isEmpty() }) {
                item(key = legend.id) {
                    val expanded = remember(expandedCategories[legend.id]) { expandedCategories[legend.id]!! }
                    ListItem(
                        modifier = Modifier
                            .animateItem()
                            .animateEnterExit()
                            .animateContentSize()
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.large)
                            .clickable {
                                expandedCategories[legend.id] = !expanded
                            },
                        headlineContent = { Text(legend.name) },
                        trailingContent = {
                            Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, null)
                        },
                        colors = viewModel.legendColors[legend]!!.colors()
                    )
                }

                if (expandedCategories[legend.id] == true) {
                    items(spaces, { it.code }) { space ->
                        SpaceListItem(
                            space,
                            Modifier
                                .animateItem()
                                .animateEnterExit()
                                .animateContentSize(),
                            viewModel
                        )
                    }
                }
            }

        }
        if (viewModel.selectedSpace != null) {
            SpaceBottomSheet(viewModel.selectedSpace!!, viewModel) { viewModel.selectedSpace = null }
        }
    }
}