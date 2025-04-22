package com.globant.ticketmaster.core.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.globant.ticketmaster.core.designsystem.R

@Composable
fun ImageEvent(
    urlImage: String,
    modifier: Modifier,
) {
    AsyncImage(
        modifier =
            modifier
                .clip(RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp)),
        contentScale = ContentScale.Crop,
        model =
            ImageRequest
                .Builder(LocalContext.current)
                .data(urlImage)
                .build(),
        placeholder = painterResource(R.drawable.placeholder),
        error = painterResource(R.drawable.placeholder),
        contentDescription = null,
    )
}
