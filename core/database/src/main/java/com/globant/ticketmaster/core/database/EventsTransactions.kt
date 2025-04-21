package com.globant.ticketmaster.core.database

import androidx.paging.LoadType
import androidx.room.withTransaction
import javax.inject.Inject

class EventsTransactionsImpl
    @Inject
    constructor(
        private val ticketMasterDatabase: TicketMasterDatabase,
    ) : EventsTransactions {
        override suspend fun deleteInsert(
            loadType: LoadType,
            insertOperation: suspend () -> Unit,
            deleteOperation: suspend () -> Unit,
            needDelete: suspend () -> Boolean,
        ) {
            ticketMasterDatabase.withTransaction {
                if (needDelete()) {
                    deleteOperation()
                }
                insertOperation()
            }
        }
    }

interface EventsTransactions {
    suspend fun deleteInsert(
        loadType: LoadType,
        insertOperation: suspend () -> Unit,
        deleteOperation: suspend () -> Unit,
        needDelete: suspend () -> Boolean,
    )
}
