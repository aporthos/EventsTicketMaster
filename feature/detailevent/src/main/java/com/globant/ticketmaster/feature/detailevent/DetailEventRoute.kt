package com.globant.ticketmaster.feature.detailevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.domain.Venues
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.feature.detailevent.components.EventTopAppBar
import kotlinx.serialization.Serializable
import com.globant.ticketmaster.core.designsystem.R as Designsystem

@Serializable
data class DetailEvent(
    val idEvent: String,
    val name: String,
)

@Composable
fun DetailEventRoute(
    name: String,
    viewModel: DetailEventViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val eventState by viewModel.eventState.collectAsStateWithLifecycle()
    DetailEventRoute(
        name = name,
        eventUiState = eventState,
        onFavoriteClick = viewModel::updateFavoriteEvent,
        onBackClick = onBackClick,
    )
}

@Composable
fun DetailEventRoute(
    name: String,
    eventUiState: EventUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Scaffold(
        topBar = {
            EventTopAppBar(
                name = name,
                onBackClick = onBackClick,
            )
        },
        content = { paddingValues ->
            when (eventUiState) {
                EventUiState.Loading -> LoadingScreen()

                is EventUiState.Success -> {
                    DetailEvent(
                        modifier = Modifier.padding(paddingValues),
                        event = eventUiState.event,
                        onFavoriteClick = onFavoriteClick,
                    )
                }
            }
        },
    )
}

@Composable
fun DetailEvent(
    modifier: Modifier = Modifier,
    event: EventUi,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        AsyncImage(
            modifier =
                Modifier
                    .fillMaxHeight(0.3f)
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
        if (event.venues.isNotEmpty()) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = event.venues.first().name,
            )
            Text(text = "${event.venues.first().city}, ${event.venues.first().stateCode}")
        }
        Text(text = event.startDateTime)
        Text(text = event.info)

        Text("Ventas disponibles:")
        Text(text = event.salesDateTime)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
            FloatingActionButton(onClick = { onFavoriteClick(event) }, shape = CircleShape) {
                Icon(
                    painter = painterResource(id = event.imageFavorite),
                    contentDescription = null,
                )
            }

            FloatingActionButton(onClick = {}, shape = CircleShape) {
                Icon(
                    painter = painterResource(id = Designsystem.drawable.share),
                    contentDescription = null,
                )
            }

            FloatingActionButton(onClick = {}, shape = CircleShape) {
                Icon(
                    painter = painterResource(id = Designsystem.drawable.location),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailEventPreview() {
    DetailEvent(
        onFavoriteClick = {},
        event =
            EventUi(
                idEvent = "blandit",
                name = "Dominic Cherry",
                type = "hinc",
                urlEvent = "https://www.google.com/#q=praesent",
                locale = "venenatis",
                urlImage = "https://duckduckgo.com/?q=persequeris",
                month = "Sept",
                day = "18",
                startDateTime = "2025-04-23T01:30:00Z",
                info = "semper",
                salesDateTime = "2025-04-23T01:30:00Z",
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
                eventType = EventType.Favorite,
                imageFavorite = Designsystem.drawable.favorite_unselected,
            ),
    )
}
