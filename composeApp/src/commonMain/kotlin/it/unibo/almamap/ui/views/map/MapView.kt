package it.unibo.almamap.ui.views.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import it.unibo.almamap.BackGestureHandler
import it.unibo.almamap.ui.components.IconButtonSelect
import it.unibo.almamap.ui.components.SpaceBottomSheet
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MapView(viewModel: MapViewModel = koinViewModel<MapViewModel>()) {
    Box(modifier = Modifier.fillMaxSize()) {
        val webViewState = rememberWebViewStateWithHTMLFile("map.html").apply {
            webSettings.apply {
                backgroundColor = MaterialTheme.colorScheme.background
                supportZoom = true
                allowFileAccessFromFileURLs = true
                allowUniversalAccessFromFileURLs = true
            }
        }
        val jsBridge = rememberWebViewJsBridge()
        val webViewNavigator = rememberWebViewNavigator()

        LaunchedEffect(jsBridge) {
            jsBridge.register(object : IJsMessageHandler {
                override fun handle(
                    message: JsMessage,
                    navigator: WebViewNavigator?,
                    callback: (String) -> Unit
                ) = viewModel.onMapClick(message.params)

                override fun methodName() = "onMapClick"
            })
        }

        LaunchedEffect(viewModel.phase) {
            viewModel.drawMap(webViewNavigator)
        }

        Column(
            Modifier
            .fillMaxWidth()
            .zIndex(2f)
            .align(Alignment.TopStart)
        ) {
            AnimatedVisibility(viewModel.loading || !viewModel.mapReady) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
            ListItem(
                leadingContent = {
                    AnimatedVisibility(viewModel.phase != MapViewModel.Phase.Campus && viewModel.phase != null) {
                        IconButton(onClick = { viewModel.onBack() }) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                        }
                        BackGestureHandler { viewModel.onBack() }
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                headlineContent = {
                    Text(
                        when (viewModel.phase) {
                            MapViewModel.Phase.Campus -> "Campus"
                            MapViewModel.Phase.Floor -> viewModel.selectedFloor?.name ?: ""
                            null -> ""
                        },
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                trailingContent = {
                    AnimatedVisibility(viewModel.phase == MapViewModel.Phase.Floor) {
                        IconButtonSelect(
                            options = viewModel.selectedBuilding?.floors?.map { it.name } ?: emptyList(),
                            onValueChangedEvent = {
                                viewModel.selectedFloor =
                                    viewModel.selectedBuilding?.floors?.find { floor -> floor.name == it }
                                viewModel.drawMap(webViewNavigator)
                            },
                            icon = Icons.Outlined.Layers,
                            contentDescription = "Select floor"
                        )
                    }
                }
            )
        }


        WebView(
            webViewState,
            webViewJsBridge = jsBridge,
            navigator = webViewNavigator,
            modifier = Modifier.align(Alignment.Center).fillMaxSize(),
            onCreated = {
                viewModel.mapReady = false
            }
        )
    }
    if (viewModel.selectedSpace != null) {
        SpaceBottomSheet(viewModel.selectedSpace!!, viewModel.spacesListViewModel) {
            viewModel.selectedSpace = null
        }
    }
}