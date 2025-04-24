package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.testing.venues
import com.globant.ticketmaster.core.testing.venuesEntity
import com.globant.ticketmaster.core.testing.venuesNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class VenuesMapperTest {
    @Test
    fun `validation venues network to domain`() {
        val result = venuesNetwork.networkToDomain()
        assertEquals(result.idVenue, venues.idVenue)
        assertEquals(result.name, venues.name)
        assertEquals(result.urlVenue, venues.urlVenue)
        assertEquals(result.city, venues.city)
        assertEquals(result.state, venues.state)
        assertEquals(result.stateCode, venues.stateCode)
        assertEquals(result.country, venues.country)
        assertEquals(result.address, venues.address)
    }

    @Test
    fun `validation venues domain to entity`() {
        val result = venues.domainToEntity(idEvent = "novum")
        assertEquals(result.idVenue, venues.idVenue)
        assertEquals(result.name, venues.name)
        assertEquals(result.urlVenue, venues.urlVenue)
        assertEquals(result.city, venues.city)
        assertEquals(result.state, venues.state)
        assertEquals(result.stateCode, venues.stateCode)
        assertEquals(result.country, venues.country)
        assertEquals(result.address, venues.address)
    }

    @Test
    fun `validation venues entity to domain`() {
        val result = venuesEntity.entityToDomain()
        assertEquals(result.idVenue, venues.idVenue)
        assertEquals(result.name, venues.name)
        assertEquals(result.urlVenue, venues.urlVenue)
        assertEquals(result.city, venues.city)
        assertEquals(result.state, venues.state)
        assertEquals(result.stateCode, venues.stateCode)
        assertEquals(result.country, venues.country)
        assertEquals(result.address, venues.address)
    }
}
