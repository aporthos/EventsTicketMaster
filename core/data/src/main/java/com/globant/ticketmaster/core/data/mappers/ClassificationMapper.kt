package com.globant.ticketmaster.core.data.mappers

import com.globant.ticketmaster.core.models.domain.Classification
import com.globant.ticketmaster.core.models.entity.ClassificationsEntity
import com.globant.ticketmaster.core.models.network.classifications.ClassificationNetwork

fun ClassificationNetwork.networkToDomain(): Classification =
    Classification(
        idClassification = segment?.idSegment.orEmpty(),
        name = segment?.name.orEmpty(),
    )

fun Classification.domainToEntity(): ClassificationsEntity =
    ClassificationsEntity(
        idClassification = idClassification,
        name = name,
    )

fun ClassificationsEntity.entityToDomain(): Classification =
    Classification(
        idClassification = idClassification,
        name = name,
    )

fun List<ClassificationNetwork>.networkToDomains(): List<Classification> = map(ClassificationNetwork::networkToDomain)

fun List<Classification>.domainToEntities(): List<ClassificationsEntity> = map(Classification::domainToEntity)

fun List<ClassificationsEntity>.entityToDomains(): List<Classification> = map(ClassificationsEntity::entityToDomain)
