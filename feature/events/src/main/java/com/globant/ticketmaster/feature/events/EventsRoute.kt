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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.common.showToast
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.EventLargeItem
import com.globant.ticketmaster.core.ui.LaunchViewEffect
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.core.designsystem.R as DesignSystem

@Composable
fun EventsRoute(
    navigateToDetailEvent: (EventUi) -> Unit,
    navigateToSearch: () -> Unit,
    navigateToClassification: (Classification) -> Unit,
    navigateToLastVisited: () -> Unit,
    viewModel: EventsViewModel = hiltViewModel(),
) {
    val eventsState by viewModel.suggestionsEventsState.collectAsStateWithLifecycle()
    val classificationsState by viewModel.classificationsState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    EventsRoute(
        eventsState = eventsState,
        classificationsState = classificationsState,
        onEvents = viewModel::onTriggerEvent,
    )

    LaunchViewEffect(viewModel) { effect ->
        when (effect) {
            is EventsEffects.ShowError -> context.showToast(effect.message)
            is EventsEffects.Success -> context.showToast(effect.message)
            is EventsEffects.NavigateToDetailEvent -> navigateToDetailEvent(effect.event)
            EventsEffects.NavigateToSearch -> navigateToSearch()
            is EventsEffects.NavigateToClassification -> navigateToClassification(effect.classification)
            EventsEffects.NavigateToLastVisited -> navigateToLastVisited()
        }
    }
}

@Composable
fun EventsRoute(
    eventsState: EventsUiState,
    classificationsState: ClassificationsUiState,
    onEvents: (EventsUiEvents) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_events)) },
                actions = {
                    IconButton(onClick = {
                        onEvents(EventsUiEvents.NavigateToSearch)
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
            PullToRefreshBox(
                modifier = Modifier.padding(paddingValues),
                isRefreshing = (eventsState as? EventsUiState.Items)?.isRefreshing == true,
                onRefresh = {
                    onEvents(EventsUiEvents.OnRefresh)
                },
            ) {
                Column {
                    SectionClassifications(classificationsState, onEvents)
                    Spacer(modifier = Modifier.width(8.dp))
                    SectionEvents(
                        eventsState = eventsState,
                        onEvents = onEvents,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SectionEvents(
    eventsState: EventsUiState,
    onEvents: (EventsUiEvents) -> Unit,
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
                            TextButton(onClick = { onEvents(EventsUiEvents.NavigateToLastVisited) }) {
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
                            EventLargeItem(item, onEventClick = {
                                onEvents(EventsUiEvents.NavigateToDetail(item))
                            }, onFavoriteClick = {
                                onEvents(EventsUiEvents.UpdateFavoriteEvent(item))
                            })
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
                    EventLargeItem(item, onEventClick = {
                        onEvents(EventsUiEvents.NavigateToDetail(item))
                    }, onFavoriteClick = {
                        onEvents(EventsUiEvents.UpdateFavoriteEvent(item))
                    })
                }
            }
        }
    }
}

@Composable
fun SectionClassifications(
    classificationsState: ClassificationsUiState,
    onEvents: (EventsUiEvents) -> Unit,
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
                                .clickable { onEvents(EventsUiEvents.NavigateToClassification(item)) },
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
