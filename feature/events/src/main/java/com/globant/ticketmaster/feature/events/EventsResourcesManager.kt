package com.globant.ticketmaster.feature.events

import android.content.res.Resources
import javax.inject.Inject

class EventsResourcesManagerImpl
    @Inject
    constructor(
        private val resources: Resources,
    ) : EventsResourcesManager {
        override val errorMessage: String
            get() = resources.getString(R.string.error_message)
        override val successMessage: String
            get() = resources.getString(R.string.success_message)
    }

interface EventsResourcesManager {
    val errorMessage: String
    val successMessage: String
}
