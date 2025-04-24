package com.globant.ticketmaster.feature.onboarding

import com.globant.ticketmaster.core.models.ui.ScreenItem

sealed interface OnboardingUiState {
    data object Loading : OnboardingUiState

    data class Items(
        val items: List<ScreenItem>,
    ) : OnboardingUiState
}
