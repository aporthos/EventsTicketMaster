package com.globant.ticketmaster.feature.countries

import com.globant.ticketmaster.core.common.COUNTRY_AU
import com.globant.ticketmaster.core.common.COUNTRY_ES
import com.globant.ticketmaster.core.common.COUNTRY_MX
import com.globant.ticketmaster.core.common.COUNTRY_NO
import com.globant.ticketmaster.core.common.COUNTRY_US
import com.globant.ticketmaster.core.models.domain.CountryEvent

val COUNTRIES =
    listOf(
        CountryEvent("Mexico", COUNTRY_MX, true),
        CountryEvent("Estados Unidos", COUNTRY_US, false),
        CountryEvent("Australia", COUNTRY_AU, false),
        CountryEvent("España", COUNTRY_ES, false),
        CountryEvent("Noruega", COUNTRY_NO, false),
    )
