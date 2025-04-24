package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.testing.classification
import com.globant.ticketmaster.core.testing.classificationEntity
import com.globant.ticketmaster.core.testing.classificationNetwork
import org.junit.Assert.assertEquals
import org.junit.Test

class ClassificationMapperTest {
    @Test
    fun `validation classification network to domain`() {
        val result = classificationNetwork.networkToDomain()
        assertEquals(result.idClassification, classification.idClassification)
        assertEquals(result.name, classification.name)
    }

    @Test
    fun `validation classification domain to entity`() {
        val result = classification.domainToEntity()
        assertEquals(result.idClassification, classification.idClassification)
        assertEquals(result.name, classification.name)
    }

    @Test
    fun `validation classification entity to domain`() {
        val result = classificationEntity.entityToDomain()
        assertEquals(result.idClassification, classification.idClassification)
        assertEquals(result.name, classification.name)
    }
}
