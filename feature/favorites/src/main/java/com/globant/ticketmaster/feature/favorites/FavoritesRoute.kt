package com.globant.ticketmaster.feature.favorites

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EmptyScreen
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LoadingScreen

@Composable
fun FavoritesRoute(
    onEventClick: (EventUi) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val eventsState by viewModel.uiState.collectAsStateWithLifecycle()
    FavoritesRoute(
        eventsState = eventsState,
        onEventClick = onEventClick,
        onFavoriteClick = viewModel::updateFavoriteEvent,
    )
}

@Composable
fun FavoritesRoute(
    eventsState: EventsUiState,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_favorites)) },
            )
        },
        content = { paddingValues ->
            when (eventsState) {
                EventsUiState.Empty -> EmptyScreen()

                EventsUiState.Loading -> LoadingScreen()

                is EventsUiState.Favorites -> {
                    LazyColumn(
                        modifier = Modifier.padding(paddingValues),
                    ) {
                        items(eventsState.events, key = { it.idEvent }) { item ->
                            EventItem(item, onEventClick, onFavoriteClick)
                        }
                    }
                }
            }
        },
    )
}
