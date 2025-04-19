package com.globant.ticketmaster.core.database.converters

import androidx.room.TypeConverter
import com.globant.ticketmaster.core.common.EventType

class EventTypeConverter {
    @TypeConverter
    fun toType(name: String): EventType = EventType.from(name)

    @TypeConverter
    fun fromType(name: EventType): String = name.value
}
