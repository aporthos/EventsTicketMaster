package com.globant.ticketmaster.core.models.ui

import androidx.annotation.DrawableRes
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.designsystem.R
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.domain.Sales
import com.globant.ticketmaster.core.models.domain.Venues
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class EventUi(
    val idEvent: String,
    val name: String,
    val type: String,
    val urlEvent: String,
    val locale: String,
    val urlImage: String,
    val month: String,
    val day: String,
    val startDateTime: String,
    val salesDateTime: String,
    val info: String,
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
        month = startEventDateTime.totoDate("MMM"),
        day = startEventDateTime.totoDate("dd"),
        startDateTime = startEventDateTime.totoDate("EEE, dd MMM yyyy HH:mm"),
        salesDateTime = sales.salesDateTime("dd MMM HH:mm"),
        venues = venues,
        info = info,
        eventType = eventType,
        imageFavorite = getImageFavorite(eventType),
    )

fun Sales.salesDateTime(pattern: String): String = "${startDateTime.totoDate(pattern)} - ${endDateTime.totoDate(pattern)}"

fun Long.totoDate(pattern: String): String {
    if (this == 0L) {
        return ("--")
    }
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}

fun List<Event>.domainToUis(): List<EventUi> = map(Event::domainToUi)

private fun getImageFavorite(eventType: EventType): Int =
    if (eventType == EventType.Favorite) {
        R.drawable.favorite_selected
    } else {
        R.drawable.favorite_unselected
    }
