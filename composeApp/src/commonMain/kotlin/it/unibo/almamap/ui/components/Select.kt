package it.unibo.almamap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuBoxScope
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    options: List<String>,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier,
    enabled: Boolean = true,
    anchor: @Composable (ExposedDropdownMenuBoxScope.(expanded: Boolean) -> Unit),
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = !expanded
            }
        },
        modifier = modifier
    ) {
        anchor(expanded)

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = menuModifier) {
            for (option: String in options) {
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledSelect(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Select(
        options = options,
        onValueChangedEvent = onValueChangedEvent,
        modifier = modifier,
        menuModifier = menuModifier
    ) {
        TextField(
            readOnly = true,
            enabled = enabled,
            value = selectedValue,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .then(textFieldModifier)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedSelect(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    menuModifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Select(
        options = options,
        onValueChangedEvent = onValueChangedEvent,
        modifier = modifier,
        menuModifier = menuModifier
    ) {
        OutlinedTextField(
            readOnly = true,
            enabled = enabled,
            value = selectedValue,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .then(textFieldModifier)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconButtonSelect(
    options: List<String>,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    menuModifier: Modifier = Modifier,
    buttonModifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String?
) {
    Select(
        options = options,
        onValueChangedEvent = onValueChangedEvent,
        modifier = modifier,
        menuModifier = menuModifier,
    ) {
        Column(modifier = Modifier.menuAnchor(), horizontalAlignment = Alignment.End) {
            IconButton(
                enabled = enabled,
                onClick = {},
                modifier = buttonModifier
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = iconModifier
                )
            }
        }
    }
}