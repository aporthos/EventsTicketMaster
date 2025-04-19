package com.globant.ticketmaster.core.models.network

import com.globant.ticketmaster.core.models.network.links.First
import com.globant.ticketmaster.core.models.network.links.Last
import com.globant.ticketmaster.core.models.network.links.Next
import com.globant.ticketmaster.core.models.network.links.Self

data class LinksNetwork(
    val first: First?,
    val last: Last?,
    val next: Next?,
    val self: Self?,
)
