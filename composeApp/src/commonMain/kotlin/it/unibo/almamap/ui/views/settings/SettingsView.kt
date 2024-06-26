package it.unibo.almamap.ui.views.settings

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.cancel
import almamap.composeapp.generated.resources.ok
import almamap.composeapp.generated.resources.settings__device_colors_subtitle
import almamap.composeapp.generated.resources.settings__device_colors_title
import almamap.composeapp.generated.resources.settings__language_option_title
import almamap.composeapp.generated.resources.settings__language_subtitle
import almamap.composeapp.generated.resources.settings__language_title
import almamap.composeapp.generated.resources.settings__theme_option_dark
import almamap.composeapp.generated.resources.settings__theme_option_light
import almamap.composeapp.generated.resources.settings__theme_option_system
import almamap.composeapp.generated.resources.settings__theme_option_title
import almamap.composeapp.generated.resources.settings__theme_subtitle
import almamap.composeapp.generated.resources.settings__theme_title
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.InvertColors
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsSwitch
import com.maxkeppeker.sheets.core.models.base.Header
import com.maxkeppeker.sheets.core.models.base.SelectionButton
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.list.ListDialog
import com.maxkeppeler.sheets.list.models.ListOption
import com.maxkeppeler.sheets.list.models.ListSelection
import it.unibo.almamap.PlatformType
import it.unibo.almamap.ThemeOptions
import it.unibo.almamap.openAppLanguageSettings
import it.unibo.almamap.platform
import it.unibo.almamap.utils.Languages
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel = koinViewModel<SettingsViewModel>()) {
    val themeDialogState: UseCaseState = rememberUseCaseState()
    val languageDialogState: UseCaseState = rememberUseCaseState()

    SettingsGroup {
        SettingsMenuLink(
            title = { Text(stringResource(Res.string.settings__theme_title)) },
            subtitle = { Text(stringResource(Res.string.settings__theme_subtitle)) },
            icon = { Icon(Icons.Outlined.Palette, null) },
            onClick = { themeDialogState.show() }
        )

        if (platform.type == PlatformType.ANDROID && platform.version >= 31) {
            SettingsSwitch(
                viewModel.useDeviceColors,
                onCheckedChange = { viewModel.useDeviceColors = it },
                title = { Text(stringResource(Res.string.settings__device_colors_title)) },
                subtitle = { Text(stringResource(Res.string.settings__device_colors_subtitle)) },
                icon = { Icon(Icons.Outlined.InvertColors, null) }
            )
        }

        val supportsSystemAppLanguage = remember { platform.type == PlatformType.IOS || (platform.type == PlatformType.ANDROID && platform.version >= 33) }
        SettingsMenuLink(
            title = { Text(stringResource(Res.string.settings__language_title)) },
            subtitle = { Text(stringResource(Res.string.settings__language_subtitle)) },
            icon = { Icon(Icons.Outlined.Language, null) },
            action = if (supportsSystemAppLanguage) {
                { Icon(Icons.AutoMirrored.Outlined.OpenInNew, null) }
            } else null,
            onClick = {
                if (supportsSystemAppLanguage) openAppLanguageSettings() else languageDialogState.show()
            }
        )
    }

    ListDialog(
        state = themeDialogState,
        selection = ListSelection.Single(
            listOf(
                ListOption(titleText = stringResource(Res.string.settings__theme_option_system), selected = viewModel.theme == ThemeOptions.FOLLOW_SYSTEM.name),
                ListOption(titleText = stringResource(Res.string.settings__theme_option_light), selected = viewModel.theme == ThemeOptions.LIGHT.name),
                ListOption(titleText = stringResource(Res.string.settings__theme_option_dark), selected = viewModel.theme == ThemeOptions.DARK.name)
            ),
            showRadioButtons = true,
            positiveButton = SelectionButton(stringResource(Res.string.ok)),
            negativeButton = SelectionButton(stringResource(Res.string.cancel))
        ) { index, _ ->
            viewModel.theme = ThemeOptions.entries[index].name
        },
        header = Header.Default(stringResource(Res.string.settings__theme_option_title))
    )

    val selectedLanguage by viewModel.languageFlow.collectAsState(initial = "EN")
    println("Selected language: $selectedLanguage")

    ListDialog(
        state = languageDialogState,
        selection = ListSelection.Single(
            Languages.entries.map { ListOption(titleText = it.label, selected = selectedLanguage == it.name) },
            showRadioButtons = true,
            positiveButton = SelectionButton(stringResource(Res.string.ok)),
            negativeButton = SelectionButton(stringResource(Res.string.cancel))
        ) { index, _ ->
            viewModel.changeLanguage(Languages.entries[index].name)
        },
        header = Header.Default(stringResource(Res.string.settings__language_option_title))
    )
}