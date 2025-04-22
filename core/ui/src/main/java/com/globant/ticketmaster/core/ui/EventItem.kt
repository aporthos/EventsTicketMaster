package com.globant.ticketmaster.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.globant.ticketmaster.core.designsystem.R
import com.globant.ticketmaster.core.models.ui.EventUi

@Composable
fun LazyItemScope.EventItem(
    event: EventUi,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxHeight()
                .animateItem()
                .padding(8.dp)
                .clickable { onEventClick(event) },
    ) {
        Row {
            ImageEvent(
                event.urlImage,
                Modifier.size(130.dp),
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Row {
                    Row {
                        Text(
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                            text = event.name,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        IconFavorite(event.imageFavorite) {
                            onFavoriteClick(event)
                        }
                    }
                }
                Row {
                    RoundShapeAlpha {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = "${event.date} \u2022 ${event.segment}",
                            color = Color.White,
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.location),
                        contentDescription = null,
                    )
                    Column {
                        Text(
                            text = event.locationPlace,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = event.locationAddress,
                            style = MaterialTheme.typography.labelSmall,
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
    LazyColumn {
        item {
            EventItem(event, {}, {})
        }
    }
}
