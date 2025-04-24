package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.testing.sales
import com.globant.ticketmaster.core.testing.salesEntity
import com.globant.ticketmaster.core.testing.salesNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class SalesMapperTest {
    @Test
    fun `validation sales network to domain`() {
        val result = salesNetwork.networkToDomain()
        assertEquals(result.startDateTime, sales.startDateTime)
        assertEquals(result.endDateTime, sales.endDateTime)
    }

    @Test
    fun `validation sales domain to entity`() {
        val result = sales.domainToEntity()
        assertEquals(result.startDateTime, sales.startDateTime)
        assertEquals(result.endDateTime, sales.endDateTime)
    }

    @Test
    fun `validation sales entity to domain`() {
        val result = salesEntity.entityToDomain()
        assertEquals(result.startDateTime, sales.startDateTime)
        assertEquals(result.endDateTime, sales.endDateTime)
    }
}
