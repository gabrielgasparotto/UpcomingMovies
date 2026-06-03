package com.example.upcomingmovies.feature.core.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

const val US_DATE_FORMAT = "yyyy-MM-dd"
const val DEFAULT_DATE_FORMAT = "dd/MM/yyyy"
const val DEFAULT_VALUE = 0
const val INVALID_DATE_FORMAT_MESSAGE = "Invalid date format: %s. Expected format: $US_DATE_FORMAT"

private val inputFormatter = ThreadLocal.withInitial {
    SimpleDateFormat(US_DATE_FORMAT, Locale.US)
}
private val outputFormatter = ThreadLocal.withInitial {
    SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault())
}

internal fun String.formatToDefaultDate(): String = try {
    inputFormatter.get()!!.parse(this)?.let { outputFormatter.get()!!.format(it) } ?: this
} catch (e: Exception) {
    throw IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE.format(this), e)
}

internal fun String.daysUntilRelease(): Long = try {
    val releaseMs = inputFormatter.get()!!.parse(this)?.time ?: return 0L
    val todayMs = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, DEFAULT_VALUE)
        set(Calendar.MINUTE, DEFAULT_VALUE)
        set(Calendar.SECOND, DEFAULT_VALUE)
        set(Calendar.MILLISECOND, DEFAULT_VALUE)
    }.timeInMillis
    TimeUnit.MILLISECONDS.toDays(releaseMs - todayMs)
} catch (e: Exception) {
    throw IllegalArgumentException(INVALID_DATE_FORMAT_MESSAGE.format(this), e)
}
