package com.globant.ticketmaster.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.globant.ticketmaster.core.designsystem.R
import com.globant.ticketmaster.core.models.ui.EventUi

@Composable
fun LazyItemScope.EventLargeItem(
    event: EventUi,
    onEventClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillParentMaxWidth()
                .padding(8.dp)
                .clickable { onEventClick(event) },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
        ) {
            Box {
                ImageEvent(
                    event.urlImage,
                    Modifier.height(180.dp),
                )

                Row(
                    modifier =
                        Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.TopStart),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RoundShapeAlpha {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = "${event.date} \u2022 ${event.segment}",
                            color = Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconFavorite(event.imageFavorite) {
                        onFavoriteClick(event)
                    }
                }

                Column(
                    modifier =
                        Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.3f),
                            ).padding(8.dp),
                ) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        text = event.name,
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

@Composable
fun RoundShapeAlpha(
    color: Color = Color.Black.copy(alpha = 0.3f),
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .background(
                    color = color,
                    shape = CircleShape,
                ),
    ) {
        content()
    }
}

@Preview
@Composable
fun EventDetailItemPreview() {
    LazyColumn {
        item {
            EventLargeItem(event, {}, {})
        }
    }
}
