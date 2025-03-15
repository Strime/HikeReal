package com.strime.hikereal.ui.util

import java.util.Locale

 fun Float.toFormattedDistance(locale: Locale = Locale.getDefault()): String {
    val distanceKm = this / 1000
    return String.format(locale, "%.1f km", distanceKm)
}

 fun Long.toFormattedDuration(locale: Locale = Locale.getDefault()): String {
    val seconds = (this / 1000) % 60
    val minutes = (this / (1000 * 60)) % 60
    val hours = (this / (1000 * 60 * 60))

    return String.format(locale, "%02d:%02d:%02d", hours, minutes, seconds)
}
