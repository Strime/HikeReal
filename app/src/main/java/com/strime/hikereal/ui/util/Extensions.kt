package com.strime.hikereal.ui.util

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.palette.graphics.Palette
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

fun Bitmap.extractDominantColorFromBitmap(): Color {
    val bitmapForProcessing = if (config == Bitmap.Config.HARDWARE) {
        copy(Bitmap.Config.ARGB_8888, true)
    } else {
        this
    }

    val palette = Palette.from(bitmapForProcessing).generate()
    val dominantSwatch = palette.dominantSwatch

    if (bitmapForProcessing != this) {
        bitmapForProcessing.recycle()
    }

    return if (dominantSwatch != null) {
        Color(dominantSwatch.rgb)
    } else {
        Color.White
    }
}

/**
 * Extension function on Color to determine the best contrasting text color (black or white)
 * based on the background color's luminance.
 *
 * @return Color.White for dark backgrounds, Color.Black for light backgrounds
 */
fun Color.getAccessibleTextColor(): Color {
    // Convert to grayscale using perceived brightness formula
    // Formula: (R * 0.299 + G * 0.587 + B * 0.114)
    val brightness = red * 0.299f + green * 0.587f + blue * 0.114f

    // WCAG recommends 0.5 as threshold
    return if (brightness < 0.5f) Color.White else Color.Black
}
