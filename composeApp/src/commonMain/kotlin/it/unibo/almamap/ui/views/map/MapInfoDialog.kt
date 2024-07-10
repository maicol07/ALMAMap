package it.unibo.almamap.ui.views.map

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.map__info_dialog_text
import almamap.composeapp.generated.resources.map__info_dialog_title
import almamap.composeapp.generated.resources.ok
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import it.unibo.almamap.data.Legend
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class, ExperimentalFoundationApi::class)
@Composable
fun MapInfoDialog(viewModel: MapViewModel = koinViewModel<MapViewModel>()) {
    AlertDialog(
        onDismissRequest = { viewModel.infoDialogOpened = false },
        title = { Text(stringResource(Res.string.map__info_dialog_title)) },
        text = {
            LazyColumn(contentPadding = PaddingValues(8.dp)) {
                item { Text(stringResource(Res.string.map__info_dialog_text)) }
                if (viewModel.isLegendLoading) {
                    items(3) {
                        LegendItem(
                            Legend("building", 0, "Loading", "Loading", 0),
                            Modifier
                                .animateItemPlacement()
                                .animateContentSize()
                                .placeholder(true, highlight = PlaceholderHighlight.fade())
                        )
                    }
                }
                items(viewModel.legend.filter { it.icon != null }) { legend ->
                    LegendItem(
                        legend,
                        Modifier.animateItemPlacement().animateContentSize()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { viewModel.infoDialogOpened = false }) {
                Text(
                    stringResource(Res.string.ok)
                )
            }
        },
        icon = { Icon(Icons.Outlined.Info, contentDescription = null) }
    )
}

@Composable
fun LegendItem(legend: Legend, modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Box(Modifier.size(30.dp, 48.dp), contentAlignment = Alignment.BottomCenter) {
                legend.icon?.let {
                    Icon(
                        if (it is DrawableResource)
                            painterResource(it)
                        else rememberVectorPainter(it as ImageVector),
                        null,
                        Modifier
                            .zIndex(2f)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, Color(69, 90, 100), CircleShape)
                            .padding(4.dp)
                            .align(Alignment.Center)
                    )
                }
                Box(Modifier.size(39.dp, 19.dp).background(legend.color))
            }
        },
        headlineContent = {
            Text(legend.name)
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}