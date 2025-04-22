package com.globant.ticketmaster.core.models.network.events

data class SalesNetwork(
    val public: PublicNetwork?,
) {
    data class PublicNetwork(
        val startDateTime: String?,
        val endDateTime: String?,
    )
}
