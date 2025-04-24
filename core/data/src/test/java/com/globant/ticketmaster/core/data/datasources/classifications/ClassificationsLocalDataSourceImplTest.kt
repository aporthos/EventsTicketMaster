package com.globant.ticketmaster.core.data.datasources.classifications

import com.globant.ticketmaster.core.database.daos.ClassificationsDao
import com.globant.ticketmaster.core.testing.MainDispatcherRule
import com.globant.ticketmaster.core.testing.classification
import com.globant.ticketmaster.core.testing.classificationEntity
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
class ClassificationsLocalDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var dao: ClassificationsDao

    private val localDataSource by lazy {
        ClassificationsLocalDataSourceImpl(dao, mainDispatcherRule.testDispatcher)
    }

    @Test
    fun `validation get classifications`() =
        runTest {
            whenever(dao.getAllClassifications()).thenReturn(
                flowOf(
                    listOf(classificationEntity, classificationEntity),
                ),
            )
            val result = localDataSource.getClassificationsStream()
            assertEquals(result.first().size, 2)
        }

    @Test
    fun `validation add classifications`() =
        runTest {
            whenever(dao.insertOrIgnore(listOf(classificationEntity))).thenReturn(listOf(1L))
            val result = localDataSource.addClassifications(listOf(classification))
            assertEquals(result, true)
        }
}
