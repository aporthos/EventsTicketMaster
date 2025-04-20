package com.globant.ticketmaster.feature.events

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EventItem
import com.globant.ticketmaster.core.ui.LoadingScreen

@Composable
fun EventsRoute(
    onEventClick: (EventUi) -> Unit,
    viewModel: EventsViewModel = hiltViewModel(),
) {
    val eventsState by viewModel.suggestionsEventsState.collectAsStateWithLifecycle()
    val classificationsState by viewModel.classificationsState.collectAsStateWithLifecycle()
    EventsRoute(
        eventsState = eventsState,
        classificationsState = classificationsState,
        onEventClick = onEventClick,
        onFavoriteClick = viewModel::updateFavoriteEvent,
    )
}

@Composable
fun EventsRoute(
    eventsState: EventsUiState,
    classificationsState: ClassificationsUiState,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_events)) },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                SectionClassifications(classificationsState)
                SectionEvents(eventsState, onEventClick, onFavoriteClick)
            }
        },
    )
}

@Composable
fun SectionEvents(
    eventsState: EventsUiState,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    when (eventsState) {
        EventsUiState.Error -> {
        }

        EventsUiState.Loading -> LoadingScreen()

        is EventsUiState.Items -> {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(R.string.section_suggestions),
                style = MaterialTheme.typography.titleLarge,
            )
            LazyColumn {
                items(eventsState.suggestionsEvents, key = { it.idEvent }) { item ->
                    EventItem(item, onEventClick, onFavoriteClick)
                }
            }
        }
    }
}

@Composable
fun SectionClassifications(classificationsState: ClassificationsUiState) {
    when (classificationsState) {
        ClassificationsUiState.Error -> {
        }

        ClassificationsUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is ClassificationsUiState.Items -> {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
            ) {
                items(
                    classificationsState.classifications,
                    key = { it.idClassification },
                ) { item ->
                    Card(
                        modifier = Modifier.padding(horizontal = 2.dp),
                    ) {
                        Text(
                            style = MaterialTheme.typography.titleLarge,
                            modifier =
                                Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 4.dp,
                                ),
                            text = item.name,
                        )
                    }
                }
            }
        }
    }
}
