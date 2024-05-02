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
import androidx.compose.material.icons.outlined.InvertColors
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
import it.unibo.almamap.platform
import it.unibo.almamap.utils.Languages
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsView(viewModel: SettingsViewModel = viewModel<SettingsViewModel>()) {
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

        SettingsMenuLink(
            title = { Text(stringResource(Res.string.settings__language_title)) },
            subtitle = { Text(stringResource(Res.string.settings__language_subtitle)) },
            icon = { Icon(Icons.Outlined.Language, null) },
            onClick = { languageDialogState.show() }
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

    ListDialog(
        state = languageDialogState,
        selection = ListSelection.Single(
            Languages.entries.map { ListOption(titleText = it.label, selected = viewModel.language == it.name) },
            showRadioButtons = true,
            positiveButton = SelectionButton(stringResource(Res.string.ok)),
            negativeButton = SelectionButton(stringResource(Res.string.cancel))
        ) { index, _ ->
            viewModel.changeLanguage(Languages.entries[index].name)
        },
        header = Header.Default(stringResource(Res.string.settings__language_option_title))
    )
}