package com.globant.ticketmaster.core.models.ui

import androidx.annotation.DrawableRes
import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.designsystem.R
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.domain.Sales
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
    val date: String,
    val locationPlace: String,
    val locationAddress: String,
    val locationCoordinates: Location,
    val startDateTime: String,
    val salesDateTime: String,
    val info: String,
    val segment: String,
    val seatMap: String,
    val eventType: EventType,
    @DrawableRes val imageFavorite: Int,
) {
    val locationNavigation: String =
        "google.navigation:q=$${locationCoordinates.latitude},${locationCoordinates.longitude}&mode=d"
}

fun Event.domainToUis() =
    EventUi(
        idEvent = idEvent,
        name = name,
        type = type,
        urlEvent = urlEvent,
        locale = locale,
        urlImage = urlImage,
        date = startEventDateTime.totoDate("MMM dd"),
        startDateTime = startEventDateTime.totoDate("EEE, dd MMM yyyy HH:mm"),
        salesDateTime = sales.salesDateTime("dd MMM HH:mm"),
        locationPlace = venues.firstOrNull()?.name.orEmpty(),
        locationAddress = "${venues.firstOrNull()?.city.orEmpty()}, ${venues.firstOrNull()?.stateCode.orEmpty()}",
        locationCoordinates = venues.firstOrNull()?.location ?: Location(0.0, 0.0),
        segment = segment,
        info = info,
        eventType = eventType,
        seatMap = seatMap,
        imageFavorite = getImageFavorite(eventType),
    )

fun Sales.salesDateTime(pattern: String): String = "${startDateTime.totoDate(pattern)} - ${endDateTime.totoDate(pattern)}"

fun Long.totoDate(pattern: String): String {
    if (this == 0L) {
        return ("--")
    }
    return SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))
}

fun List<Event>.domainToUis(): List<EventUi> = map(Event::domainToUis)

private fun getImageFavorite(eventType: EventType): Int =
    if (eventType == EventType.Favorite) {
        R.drawable.favorite_selected
    } else {
        R.drawable.favorite_unselected
    }
