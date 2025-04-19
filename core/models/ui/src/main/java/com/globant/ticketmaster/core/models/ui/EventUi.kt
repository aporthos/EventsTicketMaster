package com.globant.ticketmaster.core.models.ui

import androidx.annotation.DrawableRes
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.designsystem.R
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.domain.Venues

data class EventUi(
    val idEvent: String,
    val name: String,
    val type: String,
    val urlEvent: String,
    val locale: String,
    val urlImage: String,
    val startDateTime: String,
    val venues: List<Venues>,
    val eventType: EventType,
    @DrawableRes val imageFavorite: Int,
)

fun Event.domainToUi() =
    EventUi(
        idEvent = idEvent,
        name = name,
        type = type,
        urlEvent = urlEvent,
        locale = locale,
        urlImage = urlImage,
        startDateTime = startDateTime,
        venues = venues,
        eventType = eventType,
        imageFavorite = getImageFavorite(eventType),
    )

fun List<Event>.domainToUis(): List<EventUi> = map(Event::domainToUi)

private fun getImageFavorite(eventType: EventType): Int =
    if (eventType == EventType.Favorite) {
        R.drawable.favorite_selected
    } else {
        R.drawable.favorite_unselected
    }
