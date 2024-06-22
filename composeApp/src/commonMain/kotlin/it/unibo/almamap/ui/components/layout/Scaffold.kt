package it.unibo.almamap.ui.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.unibo.almamap.AppRoutes
import it.unibo.almamap.ui.components.TopAppBarSearch
import it.unibo.almamap.ui.components.TopAppBarSurface

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(navController: NavController, content: @Composable (PaddingValues) -> Unit) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = backStackEntry?.destination?.route?.let { AppRoutes.valueOf(it) }
    Scaffold(
        topBar = {
            Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
                AnimatedVisibility(TopAppBarState.Search.visible) {
                    TopAppBarSurface {
                        TopAppBarSearch()
                    }
                }
                AnimatedVisibility(!TopAppBarState.Search.visible) {
                    AppTopBar(
                        currentScreen = currentScreen,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() }
                    )
                }
            }
        },
        bottomBar = {
            AppBottomBar(currentScreen = currentScreen, navController = navController)
        },
        floatingActionButton = {
            AnimatedVisibility(FloatingActionButtonState.visible) {
                FloatingActionButton(
                    onClick = FloatingActionButtonState.onClick,
                    content = {
                        FloatingActionButtonState.icon?.let { Icon(it, contentDescription = FloatingActionButtonState.contentDescription) }
                    }
                )
            }
        },
        content = content
    )
}