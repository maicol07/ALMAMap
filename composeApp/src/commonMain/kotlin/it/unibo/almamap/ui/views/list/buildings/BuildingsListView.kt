package it.unibo.almamap.ui.views.list.buildings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun BuildingsListView() {
    val viewModel = viewModel(BuildingsListViewModel::class)
    Column {
        LazyColumn {
            items(viewModel.buildings, { it.id }) { building ->
                ListItem(
                    headlineContent = { Text(building.name) },
                    supportingContent = { Text(building.description) }
                )
            }
        }
    }
}