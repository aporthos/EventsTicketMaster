package com.globant.ticketmaster

import androidx.annotation.DrawableRes
import com.globant.ticketmaster.core.designsystem.R as Designsystem

const val ROUTE_EVENTS = "events"
const val ROUTE_FAVORITES = "favorites"

sealed class HomeSections(
    val title: Int,
    @DrawableRes val icon: Int,
    val route: String,
) {
    data object Events :
        HomeSections(
            title = R.string.title_events,
            icon = Designsystem.drawable.events,
            route = ROUTE_EVENTS,
        )

    data object Favorites :
        HomeSections(
            title = R.string.title_favorites,
            icon = Designsystem.drawable.favorite_selected,
            route = ROUTE_FAVORITES,
        )
}
