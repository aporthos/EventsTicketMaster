package com.globant.ticketmaster.feature.favorites

import com.globant.ticketmaster.core.models.ui.CountryEventUi
import com.globant.ticketmaster.core.models.ui.EventUi
import com.globant.ticketmaster.core.ui.ViewEffect
import com.globant.ticketmaster.core.ui.ViewEvent

sealed interface FavoritesEvents : ViewEvent {
    data class OnUpdateFavoriteEvent(
        val event: EventUi,
    ) : FavoritesEvents

    data class OnSelectCountry(
        val country: CountryEventUi,
    ) : FavoritesEvents

    data class OnSearch(
        val search: String,
    ) : FavoritesEvents

    data class NavigateToDetailEvent(
        val event: EventUi,
    ) : FavoritesEvents
}

sealed interface FavoritesEffects : ViewEffect {
    data class NavigateToDetailEvent(
        val event: EventUi,
    ) : FavoritesEffects
}
