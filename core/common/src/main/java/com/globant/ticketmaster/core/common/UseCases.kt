package com.globant.ticketmaster.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

abstract class FlowSingleUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher,
) {
    operator fun invoke(params: P): Flow<R> = execute(params).flowOn(dispatcher)

    protected abstract fun execute(params: P): Flow<R>
}

abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher,
) {
    suspend operator fun invoke(params: P): R =
        withContext(coroutineDispatcher) {
            execute(params)
        }

    protected abstract suspend fun execute(params: P): R
}
