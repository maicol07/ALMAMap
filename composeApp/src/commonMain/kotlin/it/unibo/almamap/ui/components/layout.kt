package it.unibo.almamap.ui.components

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.app_name
import almamap.composeapp.generated.resources.back
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import it.unibo.almamap.AppBarSearchBar
import it.unibo.almamap.AppRoutes
import it.unibo.almamap.NavBarItems
import it.unibo.almamap.utils.TopAppBarState
import org.jetbrains.compose.resources.stringResource

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
                        var active by remember { mutableStateOf(false) }
                        AppBarSearchBar(active = active, onActiveChange = { active = it }) {}
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
        content = content
    )
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: AppRoutes?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen?.title ?: Res.string.app_name)) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.back)
                    )
                }
            }
        }
    )
}

@Composable
fun AppBottomBar(
    currentScreen: AppRoutes?,
    navController: NavController
) {
    NavigationBar {
        for (item in NavBarItems.entries) {
            val selected = remember(currentScreen) { currentScreen == item.route }
            NavigationBarItem(
                icon = { if (selected) item.selectedIcon() else item.unselectedIcon() },
                label = { Text(item.title?.let { stringResource(it) } ?: item.route.title?.let { stringResource(it) } ?: "") },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route.name) {
                            popUpTo(navController.graph.startDestinationRoute!!) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                        // When we click again on a bottom bar item and it was already selected
                        // we want to pop the back stack until the initial destination of this bottom bar item
                        navController.popBackStack(item.route.name, false)
                    }
                }
            )
        }
    }
}