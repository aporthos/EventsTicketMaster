package com.globant.ticketmaster.feature.detailevent

import com.globant.ticketmaster.core.models.ui.EventUi

sealed interface EventUiState {
    data class Success(
        val event: EventUi,
    ) : EventUiState

    data object Loading : EventUiState
}
