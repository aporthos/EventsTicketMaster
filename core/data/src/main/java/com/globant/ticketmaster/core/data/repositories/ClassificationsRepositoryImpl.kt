package com.globant.ticketmaster.core.data.repositories

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.datasources.ClassificationsLocalDataSource
import com.globant.ticketmaster.core.data.datasources.ClassificationsRemoteDataSource
import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class ClassificationsRepositoryImpl
    @Inject
    constructor(
        private val local: ClassificationsLocalDataSource,
        private val remote: ClassificationsRemoteDataSource,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : ClassificationsRepository {
        override fun getClassifications(): Flow<List<Classification>> =
            flow {
                val cache = local.getClassifications()
                val result = remote.getClassifications()

                if (cache.first().isEmpty()) {
                    result
                        .onSuccess {
                            local.addClassifications(it)
                        }.onFailure {
                            Timber.e("getClassifications -> $it")
                        }
                }

                emitAll(cache)
            }.flowOn(ioDispatcher)
    }
