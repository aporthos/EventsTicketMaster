package com.globant.ticketmaster.core.common

enum class EventType(
    val value: String,
) {
    Default("Default"),
    Favorite("Favorite"),
    Deleted("Deleted"),
    ;

    companion object {
        fun from(value: String?): EventType = EventType.entries.find { it.value == value } ?: Default
    }
}
