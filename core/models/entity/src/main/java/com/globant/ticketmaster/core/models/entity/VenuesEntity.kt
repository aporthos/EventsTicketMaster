package com.globant.ticketmaster.core.models.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "venues",
)
data class VenuesEntity(
    @PrimaryKey val idEventVenues: String,
    val idVenue: String,
    val name: String,
    val urlVenue: String,
    val city: String,
    val state: String,
    val country: String,
    val address: String,
    @Embedded val location: LocationEntity,
)
