package it.unibo.almamap.ui.components.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

object TopAppBarState {
    object Search {
        var searchIcon by mutableStateOf(Icons.Filled.Search)
        var visible by mutableStateOf(true)
    }


    fun restoreDefaults() {
        Search.searchIcon = Icons.Filled.Search
        Search.visible = true
    }
}

object FloatingActionButtonState {
    var visible by mutableStateOf(true)
    var onClick by mutableStateOf({})
    var icon by mutableStateOf<ImageVector?>(null)
    var contentDescription by mutableStateOf<String?>(null)

    fun restoreDefaults() {
        visible = true
        onClick = {}
        icon = null
        contentDescription = null
    }
}