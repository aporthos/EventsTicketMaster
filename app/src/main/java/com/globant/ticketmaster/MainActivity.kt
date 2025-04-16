package com.globant.ticketmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.globant.ticketmaster.core.designsystem.theme.ChallengeTicketmasterTheme
import com.globant.ticketmaster.feature.events.EventsRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeTicketmasterTheme {
                EventsRoute()
            }
        }
    }
}
