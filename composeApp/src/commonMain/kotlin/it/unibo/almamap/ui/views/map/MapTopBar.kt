package it.unibo.almamap.ui.views.map

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.back
import almamap.composeapp.generated.resources.map__campus_title
import almamap.composeapp.generated.resources.map__info_button
import almamap.composeapp.generated.resources.map__select_floor_title
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebViewNavigator
import it.unibo.almamap.BackGestureHandler
import it.unibo.almamap.ui.components.IconButtonSelect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MapTopBar(webViewNavigator: WebViewNavigator, modifier: Modifier = Modifier, viewModel: MapViewModel = koinViewModel<MapViewModel>()) {
    Column(modifier) {
        AnimatedVisibility(viewModel.loading || !viewModel.mapReady) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        ListItem(
            leadingContent = {
                AnimatedVisibility(viewModel.phase != MapViewModel.Phase.Campus && viewModel.phase != null) {
                    IconButton(onClick = { viewModel.onBack() }) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = stringResource(
                                Res.string.back)
                        )
                    }
                    BackGestureHandler { viewModel.onBack() }
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            headlineContent = {
                Text(
                    when (viewModel.phase) {
                        MapViewModel.Phase.Campus -> stringResource(Res.string.map__campus_title)
                        MapViewModel.Phase.Floor -> viewModel.selectedFloor?.name ?: ""
                        null -> ""
                    },
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            trailingContent = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.width(IntrinsicSize.Min)) {
                    AnimatedVisibility(viewModel.phase == MapViewModel.Phase.Floor) {
                        IconButtonSelect(
                            options = viewModel.selectedBuilding?.floors?.map { it.name }
                                ?: emptyList(),
                            onValueChangedEvent = {
                                viewModel.selectedFloor =
                                    viewModel.selectedBuilding?.floors?.find { floor -> floor.name == it }
                                viewModel.drawMap(webViewNavigator)
                            },
                            icon = Icons.Outlined.Layers,
                            contentDescription = stringResource(Res.string.map__select_floor_title),
                            menuModifier = Modifier.width(200.dp)
                        )
                    }
                    IconButton(onClick = { viewModel.onInfoClick() }) {
                        Icon(Icons.Outlined.Info, contentDescription = stringResource(Res.string.map__info_button))
                    }
                }
            }
        )
    }
}