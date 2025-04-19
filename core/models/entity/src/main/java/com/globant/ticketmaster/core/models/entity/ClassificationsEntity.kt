package com.globant.ticketmaster.core.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "classifications",
)
data class ClassificationsEntity(
    @PrimaryKey val idClassification: String,
    val name: String,
)
