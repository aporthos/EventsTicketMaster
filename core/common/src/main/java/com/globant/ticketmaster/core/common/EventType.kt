package com.globant.ticketmaster.core.common

enum class EventType(
    val value: String,
) {
    Default("D"),
    Favorite("F"),
    Deleted("Deleted"),
    ;

    companion object {
        fun from(value: String?): EventType = EventType.entries.find { it.value == value } ?: Default
    }
}
