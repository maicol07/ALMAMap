package it.unibo.almamap.ui.components.layout

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import it.unibo.almamap.AppRoutes
import it.unibo.almamap.NavBarItems
import org.jetbrains.compose.resources.stringResource

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