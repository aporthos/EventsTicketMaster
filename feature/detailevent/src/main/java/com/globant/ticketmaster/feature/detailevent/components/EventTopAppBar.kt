package com.globant.ticketmaster.feature.detailevent.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.designsystem.R as Designsystem

@Composable
fun EventTopAppBar(
    event: EventUi,
    onBackClick: () -> Unit,
    onShareClick: (EventUi) -> Unit,
    onFavoriteClick: (EventUi) -> Unit,
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        title = { Text(text = "") },
        actions = {
            IconButton(
                onClick = { onShareClick(event) },
            ) {
                Icon(
                    painter = painterResource(id = Designsystem.drawable.share),
                    contentDescription = null,
                )
            }
            IconButton(
                onClick = { onFavoriteClick(event) },
            ) {
                Icon(
                    painter = painterResource(id = event.imageFavorite),
                    contentDescription = null,
                )
            }
        },
    )
}
