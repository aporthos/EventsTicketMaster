package com.globant.ticketmaster.feature.events

class FakeEventsResourcesManager : EventsResourcesManager {
    override val errorMessage: String
        get() = ""
    override val successMessage: String
        get() = ""
}
