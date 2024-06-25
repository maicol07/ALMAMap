package it.unibo.almamap.ui.views.map

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.map__list_all_spaces_in_building_floor
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import it.unibo.almamap.ui.components.layout.FloatingActionButtonState
import it.unibo.almamap.ui.components.spaces.SpaceBottomSheet
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MapView(viewModel: MapViewModel = koinViewModel<MapViewModel>()) {
    LaunchedEffect(viewModel.selectedBuilding, viewModel.selectedFloor) {
        FloatingActionButtonState.visible = viewModel.selectedBuilding != null
        FloatingActionButtonState.onClick = { viewModel.onListFabClick() }
        FloatingActionButtonState.icon = Icons.AutoMirrored.Rounded.List
        FloatingActionButtonState.contentDescription =
            getString(Res.string.map__list_all_spaces_in_building_floor)
    }
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

        MapTopBar(
            webViewNavigator,
            Modifier
                .fillMaxWidth()
                .zIndex(2f)
                .align(Alignment.TopStart)
        )


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

    if (viewModel.spacesListSheetOpened) {
        MapSpacesBottomSheet(onDismissRequest = { viewModel.spacesListSheetOpened = false })
    }

    if (viewModel.infoDialogOpened) {
        MapInfoDialog()
    }
}