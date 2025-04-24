package com.globant.ticketmaster.core.models.ui

import androidx.annotation.DrawableRes
import com.globant.ticketmaster.core.common.COUNTRY_AU
import com.globant.ticketmaster.core.common.COUNTRY_ES
import com.globant.ticketmaster.core.common.COUNTRY_MX
import com.globant.ticketmaster.core.common.COUNTRY_NO
import com.globant.ticketmaster.core.common.COUNTRY_US
import com.globant.ticketmaster.core.models.domain.CountryEvent
import com.globant.ticketmaster.core.designsystem.R as DesignSystem

data class CountryEventUi(
    val name: String,
    val countryCode: String,
    val isSelected: Boolean,
    @DrawableRes val icon: Int,
)

fun CountryEvent.domainToUi() = CountryEventUi(name, countryCode, isSelected, getIcon(countryCode))

fun CountryEventUi.uiToDomain() = CountryEvent(name, countryCode, isSelected)

fun List<CountryEvent>.domainToUis(): List<CountryEventUi> = map(CountryEvent::domainToUi)

fun getIcon(countryCode: String): Int =
    when (countryCode) {
        COUNTRY_AU -> DesignSystem.drawable.australia
        COUNTRY_MX -> DesignSystem.drawable.mexico
        COUNTRY_US -> DesignSystem.drawable.usa
        COUNTRY_NO -> DesignSystem.drawable.norway
        COUNTRY_ES -> DesignSystem.drawable.spain
        else -> DesignSystem.drawable.mexico
    }
