package com.globant.ticketmaster.feature.favorites

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
import com.globant.ticketmaster.core.ui.EmptyScreen
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LoadingScreen

@Composable
fun FavoritesRoute(
    onEventClick: (EventUi) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val eventsState = viewModel.uiState.collectAsLazyPagingItems()
    var search by remember { mutableStateOf(TextFieldValue("")) }
    FavoritesRoute(
        eventsState = eventsState,
        onEventClick = onEventClick,
        search = search,
        onFavoriteClick = viewModel::updateFavoriteEvent,
        onSearch = {
            search = it
            viewModel.onSearch(it.text)
        },
    )
}

@Composable
fun FavoritesRoute(
    eventsState: LazyPagingItems<EventUi>,
    search: TextFieldValue,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
    onSearch: (TextFieldValue) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                search = search,
                onSearch = onSearch,
            )
        },
        content = { paddingValues ->
            val loadState = eventsState.loadState

            if (loadState.refresh == LoadState.Loading) {
                LoadingScreen()
            } else {
                if (eventsState.itemCount == 0) {
                    EmptyScreen()
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
            }
        },
    )
}
