package it.unibo.almamap.ui.views.list

import almamap.composeapp.generated.resources.Res
import almamap.composeapp.generated.resources.list__buildings_title
import almamap.composeapp.generated.resources.list__spaces_title
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import it.unibo.almamap.ui.views.list.buildings.BuildingsListView
import it.unibo.almamap.ui.views.list.spaces.SpacesListView
import it.unibo.almamap.ui.views.list.spaces.SpacesListViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private var activeTab by mutableStateOf(ListViewTabs.Buildings)

enum class ListViewTabs(
    val title: StringResource,
    val content: @Composable (viewModel: SpacesListViewModel) -> Unit
) {
    Buildings(Res.string.list__buildings_title, { viewModel ->
        BuildingsListView {
            activeTab = Spaces
            viewModel.selectedBuilding = it
            viewModel.selectedFloor = null
        }
    }),
    Spaces(Res.string.list__spaces_title, { viewModel -> SpacesListView(viewModel) })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListView(viewModel: SpacesListViewModel = viewModel<SpacesListViewModel>()) {
    Column {
        PrimaryTabRow(selectedTabIndex = activeTab.ordinal) {
            for (tab in ListViewTabs.entries) {
                Tab(
                    modifier = Modifier.height(48.dp),
                    selected = activeTab == tab,
                    onClick = { activeTab = tab }
                ) {
                    Text(stringResource(tab.title), overflow = TextOverflow.Ellipsis)
                }
            }
        }

        val pagerState = rememberPagerState(pageCount = { ListViewTabs.entries.size })
        LaunchedEffect(activeTab) {
            pagerState.animateScrollToPage(activeTab.ordinal)
        }
        LaunchedEffect(pagerState.currentPage) {
            activeTab = ListViewTabs.entries[pagerState.currentPage]
        }

        HorizontalPager(pagerState) { page ->
            Column(Modifier.fillMaxSize()) {
                ListViewTabs.entries[page].content(viewModel)
            }
        }
    }
}