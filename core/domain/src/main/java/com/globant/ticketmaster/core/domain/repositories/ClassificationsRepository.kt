package com.globant.ticketmaster.core.domain.repositories

import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.flow.Flow

interface ClassificationsRepository {
    fun getClassifications(): Flow<Result<List<Classification>>>
}
