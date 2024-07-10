package it.unibo.almamap.ui.components.spaces

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.space__average_values
import almamap.composeapp.generated.resources.space__hide_historical_data
import almamap.composeapp.generated.resources.space__next_day
import almamap.composeapp.generated.resources.space__no_sensors
import almamap.composeapp.generated.resources.space__previous_day
import almamap.composeapp.generated.resources.space__show_historical_data
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberFloatLinearAxisModel
import io.github.koalaplot.core.xygraph.rememberIntLinearAxisModel
import io.ktor.util.date.WeekDay
import it.unibo.almamap.data.SensorData
import it.unibo.almamap.data.SensorTypes
import it.unibo.almamap.data.Space
import it.unibo.almamap.extensions.label
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)
@Composable
fun SpaceBottomSheet(
    space: Space,
    viewModel: SpacesListViewModel,
    onDismissRequest: () -> Unit
) {
    val building = remember(space) { space.getBuilding(viewModel.buildings)!! }
    val floor = remember(space) { space.getFloor(building)!! }
    val sensorsData = remember { mutableStateMapOf<String, SensorData>() }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(space.sensors) {
        isLoading = true
        sensorsData.clear()
        for (sensor in space.sensors) {
            val sensorData = viewModel.loadSensorData(sensor.code)
            sensorsData[sensor.code] = sensorData
        }
        isLoading = false
    }
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(Modifier.padding(16.dp)) {
            Text(space.name, style = MaterialTheme.typography.headlineLarge)
            Text("${building.name} - ${floor.name}", style = MaterialTheme.typography.titleSmall)
            space.description?.let { Text(it) }
//        Text("Posti: ${space.capacity ?: "N/A"}")
            Spacer(Modifier.height(16.dp))
            if (space.sensors.isEmpty()) {
                Text(stringResource(Res.string.space__no_sensors))
            } else {
                LazyColumn {
                    items(space.sensors) { sensor ->
                        val sensorData = remember(sensorsData.size) { sensorsData[sensor.code] }
                        val sensorStatus = remember(sensorData) {
                            sensorData?.live?.scale?.find {
                                sensorData.live.value in (it.lowerBound?.toFloat()
                                    ?: 0f)..(it.upperBound?.toFloat() ?: 100f)
                            }
                        }
                        var expanded by remember { mutableStateOf(false) }
                        val sensorType = remember(sensor) {
                            SensorTypes.valueOf(
                                sensor.type.code.replace(
                                    '.',
                                    '_'
                                )
                            )
                        }
                        ListItem(
                            modifier = Modifier.clickable { if (!isLoading) expanded = !expanded },
                            leadingContent = { Icon(sensorType.icon, null) },
                            overlineContent = { Text(stringResource(sensorType.label)) },
                            headlineContent = {
                                Text(
                                    "${(sensorData?.live?.value?.times(100))?.roundToInt()?.div(100f) ?: "N/A"} ${sensor.type.unit.symbol}",
                                    modifier = Modifier.placeholder(isLoading, highlight = PlaceholderHighlight.fade()),
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                                )
                            },
                            trailingContent = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Badge(
                                        containerColor = when (sensorStatus?.code) {
                                            "OPTIMAL" -> Color(0xff1d571e)
                                            "GOOD" -> Color(0xff418776)
                                            "MODERATE" -> Color(0xff9f6911)
                                            "POOR" -> Color(0xFFD32F2F)
                                            else -> Color.Gray
                                        },
                                        contentColor = Color.White
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(4.dp).placeholder(isLoading, highlight = PlaceholderHighlight.fade()),
                                            text = sensorStatus?.label?.uppercase() ?: "N/A",
                                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                        )
                                    }
                                    Icon(
                                        if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                        stringResource(if (expanded) Res.string.space__hide_historical_data else Res.string.space__show_historical_data)
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )

                        var weekDay by remember { mutableStateOf(0) }
                        val hoursRange = remember { 0..23 }
                        val maximumValue = sensorData?.dayOfWeekAverage?.flatten()?.maxOrNull()

                        AnimatedVisibility(expanded) {
                            Column {
                                Text(stringResource(Res.string.space__average_values))
                                XYGraph(
                                    xAxisModel = rememberIntLinearAxisModel(
                                        hoursRange,
                                        minorTickCount = 4,
//                                        minimumMajorTickSpacing = 10.dp
                                    ),
                                    yAxisModel = rememberFloatLinearAxisModel(
                                        0f..(maximumValue?.let { it.toFloat() * 1.5f } ?: 100f),
                                        minorTickCount = 2
                                    ),
//                                    yAxisTitle = "Valori medi",
//                                    xAxisTitle = "Ore",
                                    modifier = Modifier.height(200.dp)
                                ) {
                                    val barShape = MaterialTheme.shapes.extraSmall
                                    VerticalBarPlot(
                                        xData = hoursRange.toList(),
                                        yData = sensorData?.dayOfWeekAverage
                                        ?.get(weekDay)?.map { it.toFloat() } ?: emptyList(),
                                        modifier = Modifier.padding(horizontal = 9.dp),
                                        bar = {
                                            TooltipBox(
                                                TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                                state = rememberTooltipState(),
                                                tooltip = {
                                                    PlainTooltip { Text("${it}") }
                                                }
                                            ) {
                                                DefaultVerticalBar(
                                                    SolidColor(Color.Gray),
                                                    shape = barShape,
                                                    hoverElement = {
                                                        Text(
                                                            "${it}",
                                                            Modifier.padding(4.dp),
                                                            style = MaterialTheme.typography.bodyMedium
                                                        )
                                                    }
                                                )
                                            }
                                        }
                                    )
                                }

                                Spacer(Modifier.height(8.dp))

                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = { weekDay = (weekDay - 1) % 7 }) {
                                        Icon(
                                            Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                            stringResource(Res.string.space__previous_day)
                                        )
                                    }
                                    Text(stringResource(WeekDay.from(weekDay).label()))
                                    IconButton(onClick = { weekDay = (weekDay + 1) % 7 }) {
                                        Icon(
                                            Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                            stringResource(Res.string.space__next_day)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}