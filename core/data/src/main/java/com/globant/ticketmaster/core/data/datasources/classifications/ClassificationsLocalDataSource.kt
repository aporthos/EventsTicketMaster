package com.globant.ticketmaster.core.data.datasources.classifications

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.mappers.domainToEntities
import com.globant.ticketmaster.core.data.mappers.entityToDomains
import com.globant.ticketmaster.core.database.daos.ClassificationsDao
import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.entity.ClassificationsEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClassificationsLocalDataSourceImpl
    @Inject
    constructor(
        private val dao: ClassificationsDao,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : ClassificationsLocalDataSource {
        override fun getClassificationsStream(): Flow<List<Classification>> =
            dao
                .getAllClassifications()
                .map(List<ClassificationsEntity>::entityToDomains)
                .flowOn(dispatcher)

        override suspend fun addClassifications(classifications: List<Classification>): Boolean =
            withContext(dispatcher) {
                dao.insertOrIgnore(classifications.domainToEntities()).isNotEmpty()
            }
    }

interface ClassificationsLocalDataSource {
    fun getClassificationsStream(): Flow<List<Classification>>

    suspend fun addClassifications(classifications: List<Classification>): Boolean
}
