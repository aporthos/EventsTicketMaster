package com.globant.ticketmaster.feature.events

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EventLargeItem
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.core.designsystem.R as DesignSystem

@Composable
fun EventsRoute(
    onEventClick: (EventUi) -> Unit,
    onSearchClick: () -> Unit,
    onClassificationClick: (Classification) -> Unit,
    onLastVisitedClick: () -> Unit,
    viewModel: EventsViewModel = hiltViewModel(),
) {
    val eventsState by viewModel.suggestionsEventsState.collectAsStateWithLifecycle()
    val classificationsState by viewModel.classificationsState.collectAsStateWithLifecycle()
    EventsRoute(
        eventsState = eventsState,
        classificationsState = classificationsState,
        onEventClick = onEventClick,
        onClassificationClick = onClassificationClick,
        onSearchClick = onSearchClick,
        onFavoriteClick = viewModel::updateFavoriteEvent,
        onLastVisitedClick = onLastVisitedClick,
    )
}

@Composable
fun EventsRoute(
    eventsState: EventsUiState,
    classificationsState: ClassificationsUiState,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
    onSearchClick: () -> Unit,
    onClassificationClick: (Classification) -> Unit,
    onLastVisitedClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_events)) },
                actions = {
                    IconButton(onClick = {
                        onSearchClick()
                    }) {
                        Icon(
                            painter = painterResource(id = DesignSystem.drawable.search),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier.padding(paddingValues),
            ) {
                SectionClassifications(classificationsState, onClassificationClick)
                Spacer(modifier = Modifier.width(8.dp))
                SectionEvents(
                    eventsState = eventsState,
                    onEventClick = onEventClick,
                    onFavoriteClick = onFavoriteClick,
                    onLastVisitedClick = onLastVisitedClick,
                )
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionEvents(
    eventsState: EventsUiState,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
    onLastVisitedClick: () -> Unit,
) {
    when (eventsState) {
        EventsUiState.Error -> {
        }

        EventsUiState.Loading -> LoadingScreen()

        is EventsUiState.Items -> {
            LazyColumn {
                if (eventsState.lastVisitedEvents.isNotEmpty()) {
                    stickyHeader {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stringResource(R.string.section_last_visited),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            TextButton(onClick = { onLastVisitedClick() }) {
                                Text(
                                    text = stringResource(R.string.section_view_more),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
                }
                item {
                    LazyRow {
                        items(eventsState.lastVisitedEvents) { item ->
                            EventLargeItem(item, onEventClick, onFavoriteClick)
                        }
                    }
                }
                stickyHeader {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 8.dp),
                        text = stringResource(R.string.section_suggestions),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                items(eventsState.suggestionsEvents) { item ->
                    EventLargeItem(item, onEventClick, onFavoriteClick)
                }
            }
        }
    }
}

@Composable
fun SectionClassifications(
    classificationsState: ClassificationsUiState,
    onClassificationClick: (Classification) -> Unit,
) {
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
                        modifier =
                            Modifier
                                .padding(horizontal = 2.dp)
                                .clickable { onClassificationClick(item) },
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
