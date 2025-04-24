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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EmptyScreen
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LaunchViewEffect
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.feature.countries.CountriesUiState

@Composable
fun FavoritesRoute(
    navigateToDetailEvent: (EventUi) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val eventsState = viewModel.uiState.collectAsLazyPagingItems()
    val countriesState by viewModel.countriesState.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf(TextFieldValue("")) }
    FavoritesRoute(
        eventsState = eventsState,
        countriesState = countriesState,
        search = search,
        onSearch = {
            search = it
        },
        onEvents = viewModel::onTriggerEvent,
    )

    LaunchViewEffect(viewModel) { effect ->
        when (effect) {
            is FavoritesEffects.NavigateToDetailEvent -> navigateToDetailEvent(effect.event)
        }
    }
}

@Composable
fun FavoritesRoute(
    eventsState: LazyPagingItems<EventUi>,
    countriesState: CountriesUiState,
    search: TextFieldValue,
    onSearch: (TextFieldValue) -> Unit,
    onEvents: (FavoritesEvents) -> Unit,
) {
    Scaffold(
        topBar = {
            SearchTopAppBar(
                search = search,
                countriesState = countriesState,
                onEvents = onEvents,
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
                                EventItem(
                                    event = event,
                                    onEventClick = {
                                        onEvents(FavoritesEvents.NavigateToDetailEvent(event))
                                    },
                                    onFavoriteClick = {
                                        onEvents(FavoritesEvents.OnUpdateFavoriteEvent(event))
                                    },
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}
