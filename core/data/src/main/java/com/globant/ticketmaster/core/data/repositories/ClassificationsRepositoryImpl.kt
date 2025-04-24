package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.classifications.ClassificationsRemoteDataSource
import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ClassificationsRepositoryImpl
    @Inject
    constructor(
        private val local: ClassificationsLocalDataSource,
        private val remote: ClassificationsRemoteDataSource,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : ClassificationsRepository {
        override fun getClassifications(): Flow<Result<List<Classification>>> =
            object : ManagerCacheNetwork<List<Classification>>() {
                override suspend fun shouldFetch(data: List<Classification>): Boolean = data.isEmpty()

                override fun fetchLocal(): Flow<List<Classification>> = local.getClassificationsStream()

                override suspend fun fetchRemote(): Result<List<Classification>> = remote.getClassifications()

                override suspend fun saveResult(items: List<Classification>) {
                    local.addClassifications(items)
                }
            }.asFlow().flowOn(dispatcher)
    }
