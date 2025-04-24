package com.globant.ticketmaster.feature.onboarding

import android.content.res.Resources
import javax.inject.Inject

class OnboardingResourcesManagerImpl
    @Inject
    constructor(
        private val resources: Resources,
    ) : OnboardingResourcesManager {
        override val titleEvents: String
            get() = resources.getString(R.string.title_events)
        override val messageEvents: String
            get() = resources.getString(R.string.message_events)
        override val titleFavorites: String
            get() = resources.getString(R.string.title_favorites)
        override val messageFavorites: String
            get() = resources.getString(R.string.message_favorites)
        override val titleLastVisited: String
            get() = resources.getString(R.string.title_last_visited)
        override val messageLastVisited: String
            get() = resources.getString(R.string.message_last_visited)
    }

interface OnboardingResourcesManager {
    val titleEvents: String
    val messageEvents: String
    val titleFavorites: String
    val messageFavorites: String
    val titleLastVisited: String
    val messageLastVisited: String
}
