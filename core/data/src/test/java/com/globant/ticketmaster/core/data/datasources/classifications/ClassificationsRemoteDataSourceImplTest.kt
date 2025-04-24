package com.globant.ticketmaster.core.data.datasources.classifications

import com.globant.ticketmaster.core.data.ApiServices
import com.globant.ticketmaster.core.models.network.BaseResponseNetwork
import com.globant.ticketmaster.core.models.network.EmbeddedNetwork
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.classificationNetwork
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class ClassificationsRemoteDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var service: ApiServices

    private val classificationsRemoteDataSource by lazy {
        ClassificationsRemoteDataSourceImpl(service, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `validation get classifications`() =
        runTest {
            whenever(service.getClassifications()).thenReturn(
                BaseResponseNetwork(
                    embedded =
                        EmbeddedNetwork(
                            classifications = listOf(classificationNetwork),
                        ),
                    links = null,
                    page = null,
                ),
            )

            val result = classificationsRemoteDataSource.getClassifications()
            assertEquals(result.isSuccess, true)
        }

    @Test
    fun `validation get classifications failure`() =
        runTest {
            whenever(service.getClassifications()).thenThrow(RuntimeException())
            val result = classificationsRemoteDataSource.getClassifications()
            assertEquals(result.isFailure, true)
        }
}
