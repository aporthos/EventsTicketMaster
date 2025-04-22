package com.globant.ticketmaster.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.domain.Venues
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.designsystem.R as Designsystem

@Composable
fun EventItem(
    event: EventUi,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onEventClick(event) },
        shape = RoundedCornerShape(0.dp),
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    modifier =
                        Modifier
                            .size(60.dp)
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
                Text(event.month)
                Text(event.day)
            }

            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(style = MaterialTheme.typography.titleLarge, text = event.name)
                if (event.venues.isNotEmpty()) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = event.venues.first().name,
                    )
                    Text(text = "${event.venues.first().city}, ${event.venues.first().state}")
                }
                Text(text = event.segment)
                Row {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(Designsystem.drawable.share),
                            contentDescription = null,
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(Designsystem.drawable.location),
                            contentDescription = null,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onFavoriteClick(event) }) {
                        Icon(
                            painter = painterResource(event.imageFavorite),
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EventItemPreview() {
    val event =
        EventUi(
            idEvent = "G5vVZbowlaVz5",
            name = "New York Yankees vs. Toronto Blue Jays",
            type = "event",
            urlEvent = "https://www.ticketmaster.com/new-york-yankees-vs-toronto-blue-bronx-new-york-04-26-2025/event/1D00611CAAF94D41",
            locale = "en-us",
            urlImage = "https://s1.ticketm.net/dam/a/7e0/479ac7e7-15fb-44ba-8708-fc1bf2d037e0_RETINA_PORTRAIT_3_2.jpg",
            month = "Sept",
            day = "18",
            startDateTime = "2025-04-23T01:30:00Z",
            imageFavorite = Designsystem.drawable.favorite_unselected,
            eventType = EventType.Default,
            info = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            salesDateTime = "2025-04-23T01:30:00Z",
            segment = "Sports",
            venues =
                listOf(
                    Venues(
                        idVenue = "",
                        name = "Estadio GNP Seguros",
                        urlVenue = "https://www.ticketmaster.com.mx/estadio-gnp-seguros-tickets-ciudad-de-mexico/venue/163903",
                        city = "Ciudad de México",
                        state = "Ciudad de México",
                        country = "Mexico",
                        address = "Viaducto Piedad y Río Churubusco s/n Cd. Deportiva",
                        stateCode = "CDMX",
                        location =
                            Location(
                                latitude = 0.0,
                                longitude = 0.0,
                            ),
                    ),
                ),
        )
    EventItem(event, {}, {})
}
