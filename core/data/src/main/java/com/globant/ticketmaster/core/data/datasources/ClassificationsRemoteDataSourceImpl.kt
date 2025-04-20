package com.globant.ticketmaster.core.data.datasources

import com.globant.ticketmaster.core.common.IoDispatcher
import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.data.mappers.networkToDomains
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ClassificationsRemoteDataSourceImpl
    @Inject
    constructor(
        private val apiServices: ApiServices,
        @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    ) : ClassificationsRemoteDataSource {
        override suspend fun getClassifications(): Result<List<Classification>> =
            withContext(ioDispatcher) {
                try {
                    val result = apiServices.getClassifications()
                    val classifications =
                        result.embedded
                            ?.classifications
                            ?.filter { it.segment != null }
                            ?.networkToDomains() ?: emptyList()
                    Result.success(classifications)
                } catch (e: Exception) {
                    Timber.e("getClassifications -> $e")
                    Result.failure(e)
                }
            }
    }

interface ClassificationsRemoteDataSource {
    suspend fun getClassifications(): Result<List<Classification>>
}
