package it.unibo.almamap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringFlow
import it.unibo.almamap.di.appModule
import it.unibo.almamap.ui.components.AppScaffold
import it.unibo.almamap.ui.theme.AppTheme
import it.unibo.almamap.utils.TopAppBarState
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@OptIn(ExperimentalSettingsApi::class)
@Throws(Throwable::class)
@Composable
internal fun App() = KoinApplication({ modules(appModule) }) {
    AppTheme {
        val navController = rememberNavController()
        LaunchedEffect(Unit) {
            navController.addOnDestinationChangedListener { _, currentDestination, _ ->
                val previousRoute = navController.previousBackStackEntry?.destination?.route
                val currentRoute = currentDestination.route
                if (previousRoute != currentRoute) {
                    TopAppBarState.restoreDefaults()
                }
            }
        }
        val locale by koinInject<ObservableSettings>().getStringFlow(
            AvailableSettings.LANGUAGE.name,
            Locale.current.language
        )
            .collectAsState(Locale.current.language)

        AppScaffold(navController) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = AppRoutes.Map.name,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
            ) {
                for (route in AppRoutes.entries) {
                    composable(route.name, content = route.content)
                }
            }
        }
    }
}

internal expect fun openUrl(url: String?)