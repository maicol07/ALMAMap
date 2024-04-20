package it.unibo.almamap

import almamap.composeapp.generated.resources.campus
import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.navbar__list
import almamap.composeapp.generated.resources.navbar__map
import almamap.composeapp.generated.resources.navbar__settings
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import it.unibo.almamap.ui.views.settings.SettingsView
import org.jetbrains.compose.resources.StringResource


/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * enum values that represent the screens in the app
 */
enum class AppRoutes(val title: StringResource? = null, val content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit = { }) {
    Map(title = Res.string.navbar__map, content = { androidx.compose.foundation.Image(org.jetbrains.compose.resources.painterResource(
        Res.drawable.campus
    ), "") }),
    List(title = Res.string.navbar__list, content = { androidx.compose.material3.Text("LISTA")}),
    Settings(title = Res.string.navbar__settings, content = { SettingsView() })
}

enum class NavBarItems(val route: AppRoutes, val unselectedIcon: @Composable () -> Unit, val selectedIcon: @Composable () -> Unit, val title: StringResource? = null) {
    Map(AppRoutes.Map, { Icon(Icons.Outlined.Map, null) }, { Icon(Icons.Filled.Map, null) }),
    List(AppRoutes.List, { Icon(Icons.AutoMirrored.Outlined.List, null) }, { Icon(Icons.AutoMirrored.Filled.List, null) }),
    Settings(AppRoutes.Settings, { Icon(Icons.Outlined.Settings, null) }, { Icon(Icons.Filled.Settings, null) })
}
