package com.globant.ticketmaster.feature.detailevent

import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.globant.ticketmaster.core.common.shareEvent
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.ImageEvent
import com.globant.ticketmaster.core.ui.LoadingScreen
import com.globant.ticketmaster.core.ui.event
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
    val context = LocalContext.current
    DetailEventRoute(
        name = name,
        eventUiState = eventState,
        onFavoriteClick = viewModel::updateFavoriteEvent,
        onBackClick = onBackClick,
        onLocationClick = {
            val intent =
                Intent(Intent.ACTION_VIEW).apply {
                    data =
                        it.locationNavigation.toUri()
                    setPackage("com.google.android.apps.maps")
                }
            context.startActivity(intent)
        },
        onShareClick = {
            context.shareEvent(context.getString(R.string.share_event, it.urlEvent))
        },
        onBuyTicketsClick = {
            val browserIntent = Intent(Intent.ACTION_VIEW, it.urlEvent.toUri())
            context.startActivity(browserIntent)
        },
    )
}

@Composable
fun DetailEventRoute(
    name: String,
    eventUiState: EventUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
    onLocationClick: (EventUi) -> Unit,
    onShareClick: (EventUi) -> Unit,
    onBuyTicketsClick: (EventUi) -> Unit,
) {
    val event = (eventUiState as? EventUiState.Success)?.event
    Scaffold(
        topBar = {
            event?.let {
                EventTopAppBar(
                    event = event,
                    onBackClick = onBackClick,
                    onFavoriteClick = onFavoriteClick,
                    onShareClick = onShareClick,
                )
            }
        },
        content = { paddingValues ->
            when (eventUiState) {
                EventUiState.Loading -> LoadingScreen()

                is EventUiState.Success -> {
                    DetailEvent(
                        modifier = Modifier.padding(paddingValues),
                        event = eventUiState.event,
                        onLocationClick = onLocationClick,
                        onBuyTicketsClick = onBuyTicketsClick,
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
    onLocationClick: (EventUi) -> Unit,
    onBuyTicketsClick: (EventUi) -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        DetailEventsItems(event, onLocationClick)
        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp),
            onClick = { onBuyTicketsClick(event) },
        ) {
            Text(text = stringResource(R.string.buy_tickets))
        }
    }
}

@Composable
fun DetailEventsItems(
    event: EventUi,
    onLocationClick: (EventUi) -> Unit,
) {
    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize(),
    ) {
        item {
            Box {
                ImageEvent(
                    event.urlImage,
                    Modifier.fillMaxWidth(),
                )
                Column(
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                            )
                            .padding(8.dp),
                ) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        text = event.name,
                    )
                }
            }
        }
        item {
            EventItem(
                title = event.startDateTime,
                description = stringResource(R.string.start_date),
                icon = Designsystem.drawable.events,
            )
        }

        item {
            EventItem(
                title = event.locationPlace,
                description = event.locationAddress,
                icon = Designsystem.drawable.location,
            ) {
                onLocationClick(event)
            }
        }

        item {
            EventItem(
                title = event.segment,
                description = stringResource(R.string.category),
                icon = Designsystem.drawable.label,
            )
        }

        item {
            EventItem(
                title = event.salesDateTime,
                description = stringResource(R.string.date_sales),
                icon = Designsystem.drawable.sales,
            )
        }

        item {
            Column(
                modifier = Modifier.padding(8.dp),
            ) {
                Text(
                    text = stringResource(R.string.about),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = event.info,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        item {
            if (event.seatMap.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(8.dp),
                ) {
                    ImageEvent(
                        event.seatMap,
                        Modifier.fillMaxWidth(),
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun EventItem(
    title: String,
    description: String,
    @DrawableRes icon: Int,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailEventPreview() {
    DetailEvent(
        onLocationClick = {},
        onBuyTicketsClick = {},
        event = event,
    )
}
