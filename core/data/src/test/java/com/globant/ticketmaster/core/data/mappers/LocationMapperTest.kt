package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.testing.location
import com.globant.ticketmaster.core.testing.locationEntity
import com.globant.ticketmaster.core.testing.locationNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationMapperTest {
    @Test
    fun `validation location network to domain`() {
        val result = locationNetwork.networkToDomain()
        assertEquals(result.latitude, location.latitude, 0.0)
        assertEquals(result.longitude, location.longitude, 0.0)
    }

    @Test
    fun `validation location domain to entity`() {
        val result = location.domainToEntity()
        assertEquals(result.latitude, location.latitude, 0.0)
        assertEquals(result.longitude, location.longitude, 0.0)
    }

    @Test
    fun `validation location entity to domain`() {
        val result = locationEntity.entityToDomain()
        assertEquals(result.latitude, location.latitude, 0.0)
        assertEquals(result.longitude, location.longitude, 0.0)
    }
}
