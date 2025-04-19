package com.globant.ticketmaster.core.data

import com.globant.ticketmaster.core.models.network.BaseResponseNetwork
import com.globant.ticketmaster.core.models.network.classifications.ClassificationNetwork
import com.globant.ticketmaster.core.models.network.events.EventNetwork
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("discovery/v2/events")
    suspend fun getEvents(
        @Query("countryCode") countryCode: String,
    ): BaseResponseNetwork<EventNetwork>

    @GET("discovery/v2/classifications")
    suspend fun getClassifications(): BaseResponseNetwork<ClassificationNetwork>

    @GET("discovery/v2/suggest?resource=events")
    suspend fun getSuggestions(
        @Query("countryCode") countryCode: String,
    ): BaseResponseNetwork<EventNetwork>
}
