package com.globant.ticketmaster.feature.searchevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.feature.searchevent.components.SearchTopAppBar
import kotlinx.serialization.Serializable

@Serializable
data class SearchEvents(
    val idClassification: String,
)

@Composable
fun SearchEventsRoute(
    onEventClick: (EventUi) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchEventsViewModel = hiltViewModel(),
) {
    val eventsState by viewModel.eventsState.collectAsStateWithLifecycle()
    var search by remember { mutableStateOf(TextFieldValue("")) }
    SearchEventsRoute(
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
fun SearchEventsRoute(
    eventsState: EventsUiState,
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
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                when (eventsState) {
                    EventsUiState.Empty -> {
                        Text("No hay resultados")
                    }

                    EventsUiState.Loading -> LoadingScreen()

                    is EventsUiState.Success -> {
                        LazyColumn {
                            items(eventsState.events, key = { it.idEvent }) { item ->
                                EventItem(item, onEventClick, onFavoriteClick)
                            }
                        }
                    }
                }
            }
        },
    )
}
