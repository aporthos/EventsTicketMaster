package com.globant.ticketmaster.core.common

import android.content.Context
import android.content.Intent

fun Context.shareEvent(message: String) {
    val sendIntent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}
