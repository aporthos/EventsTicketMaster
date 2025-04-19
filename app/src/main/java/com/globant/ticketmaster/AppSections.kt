package com.globant.ticketmaster

import kotlinx.serialization.Serializable

sealed interface AppSections {
    @Serializable
    data object Home : AppSections

    @Serializable
    data object Onboarding : AppSections
}
