package it.unibo.almamap.ui.components.spaces

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import it.unibo.almamap.data.Space
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel

@Composable
fun SpaceListItem(space: Space, modifier: Modifier = Modifier, viewModel: SpacesListViewModel) {
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