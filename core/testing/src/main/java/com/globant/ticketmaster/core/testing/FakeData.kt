package com.globant.ticketmaster.core.testing

import com.globant.ticketmaster.core.common.EventType
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.domain.Event
import com.globant.ticketmaster.core.models.domain.Location
import com.globant.ticketmaster.core.models.domain.Sales
import com.globant.ticketmaster.core.models.domain.Venues
import com.globant.ticketmaster.core.models.entity.ClassificationsEntity
import com.globant.ticketmaster.core.models.entity.EventEntity
import com.globant.ticketmaster.core.models.entity.EventsWithVenuesEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedEventEntity
import com.globant.ticketmaster.core.models.entity.LastVisitedWithEventsEntity
import com.globant.ticketmaster.core.models.entity.LocationEntity
import com.globant.ticketmaster.core.models.entity.SalesEntity
import com.globant.ticketmaster.core.models.entity.SuggestionEventEntity
import com.globant.ticketmaster.core.models.entity.SuggestionsWithEventsEntity
import com.globant.ticketmaster.core.models.entity.VenuesEntity
import com.globant.ticketmaster.core.models.network.EmbeddedNetwork
import com.globant.ticketmaster.core.models.network.LinksNetwork
import com.globant.ticketmaster.core.models.network.classifications.ClassificationNetwork
import com.globant.ticketmaster.core.models.network.classifications.Segment
import com.globant.ticketmaster.core.models.network.events.Address
import com.globant.ticketmaster.core.models.network.events.City
import com.globant.ticketmaster.core.models.network.events.Country
import com.globant.ticketmaster.core.models.network.events.EventNetwork
import com.globant.ticketmaster.core.models.network.events.LocationNetwork
import com.globant.ticketmaster.core.models.network.events.SalesNetwork
import com.globant.ticketmaster.core.models.network.events.State
import com.globant.ticketmaster.core.models.network.events.VenuesNetwork
import com.globant.ticketmaster.core.models.ui.domainToUis

val eventDomain =
    listOf(
        Event(
            idEvent = "1AfZkA8GkdnbxmD",
            name = "Solomon Frazier",
            type = "erat",
            urlEvent = "https://search.yahoo.com/search?p=pro",
            locale = "appareat",
            urlImage = "https://duckduckgo.com/?q=a",
            startEventDateTime = 5558,
            venues = listOf(),
            eventType = EventType.Deleted,
            countryCode = "Luxembourg",
            idClassification = "suas",
            sales =
                Sales(
                    startDateTime = 6456,
                    endDateTime = 1244,
                ),
            segment = "expetenda",
            info = "sea",
            seatMap = "qui",
            page = 1638,
        ),
    )

val classificationDomain = Classification(idClassification = "persecuti", name = "Tracy Pruitt")

val eventsDomain =
    listOf(
        Event(
            idEvent = "1AfZkA8GkdnbxmD",
            name = "Solomon Frazier",
            type = "erat",
            urlEvent = "https://search.yahoo.com/search?p=pro",
            locale = "appareat",
            urlImage = "https://duckduckgo.com/?q=a",
            startEventDateTime = 5558,
            venues = listOf(),
            eventType = EventType.Deleted,
            countryCode = "Luxembourg",
            idClassification = "suas",
            sales =
                Sales(
                    startDateTime = 6456,
                    endDateTime = 1244,
                ),
            segment = "expetenda",
            info = "sea",
            seatMap = "qui",
            page = 1638,
        ),
    )
val lastVisitedEntity =
    listOf(
        LastVisitedWithEventsEntity(
            lastVisited =
                LastVisitedEventEntity(
                    idLastVisitedEvent = "tation",
                    cratedAt = 9857,
                ),
            event =
                EventEntity(
                    idEvent = "detraxit",
                    name = "Odell Valentine",
                    type = "fames",
                    urlEvent = "https://duckduckgo.com/?q=principes",
                    locale = "litora",
                    urlImage = "https://duckduckgo.com/?q=conceptam",
                    startEvent = 4462,
                    countryCode = "Japan",
                    idClassification = "vix",
                    info = "pericula",
                    segment = "mnesarchum",
                    seatMap = "viderer",
                    page = 3655,
                    sales =
                        SalesEntity(
                            startDateTime = 5938,
                            endDateTime = 1684,
                        ),
                ),
            favorite = null,
            venues = listOf(),
        ),
    )

val suggestedEntity =
    listOf(
        EventsWithVenuesEntity(
            event =
                EventEntity(
                    idEvent = "turpis",
                    name = "Laura Johnson",
                    type = "eget",
                    urlEvent = "https://www.google.com/#q=euripidis",
                    locale = "invenire",
                    urlImage = "https://www.google.com/#q=decore",
                    startEvent = 8821,
                    countryCode = "Sint Maarten",
                    idClassification = "alia",
                    info = "expetendis",
                    segment = "sententiae",
                    seatMap = "iaculis",
                    page = 7831,
                    sales =
                        SalesEntity(
                            startDateTime = 9369,
                            endDateTime = 6371,
                        ),
                ),
            venues = listOf(),
            favorite = null,
        ),
    )

val classificationEntity =
    ClassificationsEntity(idClassification = "phasellus", name = "Sherrie McClure")

val classificationNetwork =
    ClassificationNetwork(
        links =
            LinksNetwork(
                first = null,
                last = null,
                next = null,
                self = null,
            ),
        family = false,
        segment =
            Segment(
                links =
                    LinksNetwork(
                        first = null,
                        last = null,
                        next = null,
                        self = null,
                    ),
                idSegment = "phasellus",
                locale = "iudicabit",
                name = "Sherrie McClure",
                primaryId = "definitionem",
            ),
    )

val salesNetwork =
    SalesNetwork(
        public =
            SalesNetwork.PublicNetwork(
                startDateTime = "2025-05-10",
                endDateTime = "2025-05-10",
            ),
    )

val venuesNetwork =
    VenuesNetwork(
        address = Address(address = "tractatos"),
        city = City(name = "Welcome Corner"),
        country = Country(name = "Macedonia", countryCode = "123"),
        idVenue = "novum",
        images = listOf(),
        locale = null,
        location = null,
        name = "Raquel Melton",
        parkingDetail = null,
        postalCode = null,
        state = State(name = "District of Columbia", stateCode = "Florida"),
        test = null,
        timezone = null,
        type = null,
        urlVenue = "https://duckduckgo.com/?q=placerat",
    )

val venues =
    Venues(
        idVenue = "novum",
        name = "Raquel Melton",
        urlVenue = "https://duckduckgo.com/?q=placerat",
        city = "Welcome Corner",
        state = "District of Columbia",
        stateCode = "Florida",
        country = "Macedonia",
        address = "tractatos",
        location =
            Location(
                latitude = 28.29,
                longitude = 30.31,
            ),
    )

val venuesEntity =
    VenuesEntity(
        idEventVenues = "ferri",
        idVenue = "novum",
        name = "Raquel Melton",
        urlVenue = "https://duckduckgo.com/?q=placerat",
        city = "Welcome Corner",
        state = "District of Columbia",
        stateCode = "Florida",
        country = "Macedonia",
        address = "tractatos",
        location =
            LocationEntity(
                latitude = 36.37,
                longitude = 38.39,
            ),
    )
val suggestionsWithEventsEntity =
    SuggestionsWithEventsEntity(
        suggestion =
            SuggestionEventEntity(
                idEvent = "pertinax",
                createdAt = 3633,
            ),
        event =
            EventEntity(
                idEvent = "dicam",
                name = "Demetrius Carrillo",
                type = "vituperata",
                urlEvent = "https://duckduckgo.com/?q=tractatos",
                locale = "no",
                urlImage = "http://www.bing.com/search?q=dicunt",
                startEvent = 4976,
                countryCode = "Kyrgyzstan",
                idClassification = "phasellus",
                info = "debet",
                segment = "vidisse",
                seatMap = "iusto",
                page = 5122,
                sales =
                    SalesEntity(
                        startDateTime = 4964,
                        endDateTime = 9575,
                    ),
            ),
        venues = listOf(),
        favorite = null,
    )

val eventNetwork =
    EventNetwork(
        idEvent = null,
        name = null,
        type = null,
        test = null,
        urlEvent = null,
        locale = null,
        urlImages = listOf(),
        dates = null,
        seatMap = null,
        embedded =
            EmbeddedNetwork(
                classifications = listOf(),
                events = listOf(),
                venues = listOf(),
            ),
        classifications = listOf(),
        info = null,
        sales = null,
    )

val sales = Sales(startDateTime = 1746856800000, endDateTime = 1746856800000)
val salesEntity = SalesEntity(startDateTime = 1746856800000, endDateTime = 1746856800000)
val locationNetwork = LocationNetwork(latitude = "123", longitude = "456")
val location = Location(latitude = 123.0, longitude = 456.0)
val locationEntity = LocationEntity(latitude = 123.0, longitude = 456.0)
val classification = Classification(idClassification = "phasellus", name = "Sherrie McClure")
val eventUi = eventDomain.first().domainToUis()
val eventsUi = listOf(eventDomain.first().domainToUis())
