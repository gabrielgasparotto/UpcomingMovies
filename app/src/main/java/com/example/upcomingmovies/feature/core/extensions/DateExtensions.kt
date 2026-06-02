package com.example.upcomingmovies.feature.core.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
private val outputDateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale("pt", "BR"))

internal fun String.formatToBrDate(): String = try {
    inputDateFormat.parse(this)?.let { outputDateFormat.format(it) } ?: this
} catch (e: Exception) {
    this
}

internal fun String.daysUntilRelease(): Long = try {
    val releaseMs = inputDateFormat.parse(this)?.time ?: return 0L
    val todayMs = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    TimeUnit.MILLISECONDS.toDays(releaseMs - todayMs)
} catch (e: Exception) {
    0L
}

internal fun Long.toReleaseLabel(): String = when {
    this > 1 -> "Release in $this days"
    this == 1L -> "Release in 1 day"
    this == 0L -> "Releasing today"
    else -> "No ratings yet"
}

internal fun Int.formatRuntime(): String {
    val hours = this / 60
    val minutes = this % 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}
