package com.globant.ticketmaster.core.testing

import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeClassificationsRepository : ClassificationsRepository {
    private val classifications: MutableSharedFlow<List<Classification>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getClassifications(): Flow<List<Classification>> = classifications

    fun addClassification(classification: List<Classification>) {
        classifications.tryEmit(classification)
    }
}
