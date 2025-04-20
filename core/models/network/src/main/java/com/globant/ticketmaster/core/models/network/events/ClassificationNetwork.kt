package com.globant.ticketmaster.core.models.network.events

data class ClassificationNetwork(
    val family: Boolean,
    val genre: Genre,
    val primary: Boolean,
    val segment: Segment,
    val subGenre: SubGenre,
    val subType: SubType,
    val type: Type,
)
