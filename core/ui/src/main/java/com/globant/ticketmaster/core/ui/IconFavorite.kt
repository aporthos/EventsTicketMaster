package com.globant.ticketmaster.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun IconFavorite(
    @DrawableRes iconFavorite: Int,
    onFavoriteClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .padding(8.dp)
                .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
                .clip(CircleShape)
                .clickable { onFavoriteClick() },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(iconFavorite),
            tint = Color.White,
            contentDescription = null,
        )
    }
}
