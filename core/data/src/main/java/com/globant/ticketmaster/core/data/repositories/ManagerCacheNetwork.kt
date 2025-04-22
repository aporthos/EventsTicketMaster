package com.globant.ticketmaster.core.data.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

abstract class ManagerCacheNetwork<Domain> {
    fun asFlow(): Flow<Result<Domain>> =
        flow {
            if (shouldFetch(fetchLocal().first())) {
                fetchRemote()
                    .onSuccess { result ->
                        Timber.i("asFlow -> onSuccess $result")
                        saveResult(result)
                    }.onFailure { throwable ->
                        Timber.i("asFlow -> onFailure $throwable")
                        emit(Result.failure(throwable))
                    }
            }
            Timber.i("asFlow -> only db")
            emitAll(fetchLocal().map { Result.success(it) })
        }

    protected abstract suspend fun shouldFetch(data: Domain): Boolean

    protected abstract fun fetchLocal(): Flow<Domain>

    protected abstract suspend fun fetchRemote(): Result<Domain>

    protected abstract suspend fun saveResult(items: Domain)
}
