package com.globant.ticketmaster.feature.lastvisited

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.core.ui.SearchTopAppBar
import kotlinx.serialization.Serializable

@Serializable
data object LastVisited

@Composable
fun LastVisitedRoute(
    viewModel: LastVisitedViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEventClick: (EventUi) -> Unit,
) {
    val eventsState = viewModel.eventsPagingState.collectAsLazyPagingItems()
    var search by remember { mutableStateOf(TextFieldValue("")) }
    LastVisitedRoute(
        eventsState = eventsState,
        search = search,
        onBackClick = onBackClick,
        onSearch = {
            search = it
            viewModel.onSearch(it.text)
        },
        onEventClick = onEventClick,
        onFavoriteClick = viewModel::updateFavoriteEvent,
    )
}

@Composable
fun LastVisitedRoute(
    eventsState: LazyPagingItems<EventUi>,
    search: TextFieldValue,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
    onBackClick: () -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                search = search,
                onBackClick = onBackClick,
                onSearch = onSearch,
            )
        },
        content = { paddingValues ->
            val loadState = eventsState.loadState
            if (loadState.refresh == LoadState.Loading) {
                LoadingScreen()
            } else {
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                ) {
                    items(
                        count = eventsState.itemCount,
                    ) { item ->
                        eventsState[item]?.let { event ->
                            EventItem(event, onEventClick, onFavoriteClick)
                        }
                    }
                }
            }
        },
    )
}
