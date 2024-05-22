package it.unibo.almamap.ui.components

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.appbar__search
import almamap.composeapp.generated.resources.back
import almamap.composeapp.generated.resources.close
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarSearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    active: Boolean = false,
    onQueryChange: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onClose: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        SearchBar(
            modifier = if (active) {
                modifier
                    .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            } else {
                modifier
                    .padding(start = 12.dp, top = 2.dp, end = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .animateContentSize(spring(stiffness = Spring.StiffnessHigh))
            },
            query = query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            active = active,
            onActiveChange = onActiveChange,
            placeholder = { Text(stringResource(Res.string.appbar__search)) },
            leadingIcon = {
                if (active) {
                    IconButton(
                        onClick = { onActiveChange(false) },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(Res.string.back),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            },
            trailingIcon = if (active && query.isNotEmpty()) {
                {
                    IconButton(
                        onClick = {
                            onQueryChange("")
                            onClose()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(Res.string.close),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            } else {
                null
            },
            windowInsets = if (active) {
                SearchBarDefaults.windowInsets
            } else {
                WindowInsets(0.dp)
            },
            content = content
        )
    }
}

