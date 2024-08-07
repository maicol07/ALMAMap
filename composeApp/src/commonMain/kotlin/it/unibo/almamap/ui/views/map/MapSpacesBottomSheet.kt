package it.unibo.almamap.ui.views.map

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.list__no_spaces_found
import almamap.composeapp.generated.resources.map__spaces_in_building_floor
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.almamap.ui.components.spaces.SpaceListItem
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MapSpacesBottomSheet(onDismissRequest: () -> Unit, viewModel: MapViewModel = koinViewModel<MapViewModel>()) {
    ModalBottomSheet(onDismissRequest) {
        Text(
            stringResource(Res.string.map__spaces_in_building_floor, "${viewModel.selectedBuilding?.name}${viewModel.selectedFloor?.name?.let { " - $it" } ?: ""}"),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        val spaces = viewModel.spacesListViewModel.filterSpaces(viewModel.selectedBuilding!!, viewModel.selectedFloor)
        if (spaces.isEmpty()) {
            Text(
                stringResource(Res.string.list__no_spaces_found),
                modifier = Modifier.padding(16.dp)
            )
        }
        for (space in spaces) {
            SpaceListItem(
                space,
                Modifier.clickable { viewModel.selectedSpace = space },
                viewModel.spacesListViewModel
            )
        }
    }
}