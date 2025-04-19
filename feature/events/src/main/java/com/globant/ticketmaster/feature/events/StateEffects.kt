package com.globant.ticketmaster.feature.events

import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.ui.EventUi

sealed interface EventsUiState {
    data class Items(
        val suggestionsEvents: List<EventUi>,
    ) : EventsUiState

    data object Error : EventsUiState

    data object Loading : EventsUiState
}

sealed interface ClassificationsUiState {
    data class Items(
        val classifications: List<Classification>,
    ) : ClassificationsUiState

    data object Error : ClassificationsUiState

    data object Loading : ClassificationsUiState
}
