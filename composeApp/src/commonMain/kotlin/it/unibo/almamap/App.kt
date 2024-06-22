package it.unibo.almamap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unibo.almamap.di.appModules
import it.unibo.almamap.ui.components.layout.AppScaffold
import it.unibo.almamap.ui.components.layout.FloatingActionButtonState
import it.unibo.almamap.ui.components.layout.TopAppBarState
import it.unibo.almamap.ui.theme.AppTheme
import org.koin.compose.KoinContext
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

@Throws(Throwable::class)
@Composable
internal fun App() {
    remember(appModules.count()) {
        if (KoinPlatformTools.defaultContext().getOrNull() == null) {
            startKoin { modules(appModules) }
        }
    }
    KoinContext {
        AppTheme {
            val navController = rememberNavController()
            LaunchedEffect(Unit) {
                navController.addOnDestinationChangedListener { _, currentDestination, _ ->
                    val previousRoute = navController.previousBackStackEntry?.destination?.route
                    val currentRoute = currentDestination.route
                    if (previousRoute != currentRoute) {
                        TopAppBarState.restoreDefaults()
                        FloatingActionButtonState.restoreDefaults()
                    }
                }
            }


            AppScaffold(navController) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Map.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    for (route in AppRoutes.entries) {
                        composable(route.name, content = route.content)
                    }
                }
            }
        }
    }
}

internal expect fun openUrl(url: String?)