package com.globant.ticketmaster.feature.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.designsystem.R as Designsystem

@Composable
fun EventsRoute(viewModel: EventsViewModel = hiltViewModel()) {
    val eventsState by viewModel.uiState.collectAsStateWithLifecycle()
    EventsRoute(eventsState)
}

@Composable
fun EventsRoute(eventsState: EventsUiState) {
    Scaffold(topBar = {}, content = { paddingValues ->
        when (eventsState) {
            EventsUiState.Error -> {
            }

            EventsUiState.Loading -> {
            }

            is EventsUiState.Items -> {
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(eventsState.default, key = { it.idEvent }) { item ->
                        EventItem(item)
                    }
                }
            }
        }
    })
}

@Composable
fun EventItem(event: Event) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(0.dp)) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)),
                contentScale = ContentScale.Crop,
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(event.urlImage)
                        .build(),
                placeholder = painterResource(Designsystem.drawable.placeholder),
                error = painterResource(Designsystem.drawable.placeholder),
                contentDescription = null,
            )
            Column {
                Text(text = event.name)
                Text(text = "${event.venues.first().name}, ${event.venues.first().city}, ${event.venues.first().state}")
            }
        }
    }
}

@Preview
@Composable
fun EventItemPreview() {
    val event =
        Event(
            idEvent = "G5vVZbowlaVz5",
            name = "New York Yankees vs. Toronto Blue Jays",
            type = "event",
            urlEvent = "https://www.ticketmaster.com/new-york-yankees-vs-toronto-blue-bronx-new-york-04-26-2025/event/1D00611CAAF94D41",
            locale = "en-us",
            urlImage = "https://s1.ticketm.net/dam/a/7e0/479ac7e7-15fb-44ba-8708-fc1bf2d037e0_RETINA_PORTRAIT_3_2.jpg",
            startDateTime = "2025-04-27T17:35:00Z",
            venues = emptyList(),
            eventType = EventType.Default
        )
    EventItem(event)
}
