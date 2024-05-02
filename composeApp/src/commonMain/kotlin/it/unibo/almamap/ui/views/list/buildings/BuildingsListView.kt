package it.unibo.almamap.ui.views.list.buildings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Domain
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder

@Composable
fun BuildingsListView(viewModel: BuildingsListViewModel = viewModel<BuildingsListViewModel>()) {
    AnimatedVisibility(viewModel.loading) {
        LazyColumn {
            items(5) {
                ListItem(
                    headlineContent = { Text("Loading...", Modifier.placeholder(true, highlight = PlaceholderHighlight.fade()).fillMaxWidth()) },
                )
            }
        }
    }
    AnimatedVisibility(!viewModel.loading && viewModel.buildings.isEmpty()) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Rounded.WarningAmber, null, Modifier.size(64.dp))
            Text("No buildings found")
        }
    }
    AnimatedVisibility(!viewModel.loading && viewModel.buildings.isNotEmpty()) {
        LazyColumn {
            items(viewModel.buildings, { it.id }) { building ->
                ListItem(
                    headlineContent = { Text(building.name) },
                    supportingContent = if (building.description.isNotEmpty()) {
                        { Text(building.description) }
                    } else null,
                    leadingContent = { Icon(Icons.Rounded.Domain, null) },
                    trailingContent = { Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, null) },
                )
            }
        }
    }
}