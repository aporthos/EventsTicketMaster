package com.globant.ticketmaster.core.domain

import com.globant.ticketmaster.core.domain.repositories.ClassificationsRepository
import com.globant.ticketmaster.core.domain.usecases.GetClassificationsUseCase
import com.globant.ticketmaster.core.models.domain.Classification
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class GetClassificationsUseCaseTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: ClassificationsRepository

    private val useCase by lazy {
        GetClassificationsUseCase(repository, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `validation use case with data`() =
        runTest {
            whenever(repository.getClassifications()).thenReturn(flowOf(Result.success(data)))
            val result = useCase(Unit)
            assertEquals(result.first().getOrElse { emptyList() }, data)
        }

    @Test
    fun `validation use case with empty data`() =
        runTest {
            whenever(repository.getClassifications()).thenReturn(flowOf(Result.success(emptyList())))
            val result = useCase(Unit)
            assertEquals(result.first().getOrElse { emptyList() }, emptyList<Classification>())
        }

    private val data =
        listOf(
            Classification("123", "test"),
            Classification("456", "test2"),
            Classification("789", "test3"),
        )
}
