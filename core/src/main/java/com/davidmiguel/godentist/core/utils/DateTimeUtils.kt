package com.davidmiguel.godentist.core.utils

import com.google.firebase.Timestamp
import java.time.*
import java.time.format.DateTimeFormatter

fun nowToEpochMilliUtc(): Long = LocalDateTime.now()
    .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
    .toInstant()
    .toEpochMilli()

/**
 * Format LocalDateTime to dd/MM/yyyy format.
 */
fun LocalDateTime.formatDMY(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

/**
 * Format epoch milliseconds to dd/MM/yyyy format.
 */
fun Long.formatDMY(): String {
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return date.formatDMY()
}

/**
 * Format timestamp to dd/MM/yyyy format.
 */
fun Timestamp.formatDMY(): String {
    return (this.seconds * 1000 + this.nanoseconds / 1000000).formatDMY()
}

/**
 * Check whether the epoch milliseconds correspond to today.
 */
fun Long.isToday(): Boolean {
    return LocalDate.ofEpochDay(this / (24 * 60 * 60 * 1000)).isEqual(LocalDate.now())
}

/**
 * Check whether the epoch milliseconds correspond to today.
 */
fun Timestamp.isToday(): Boolean {
    return (this.seconds * 1000 + this.nanoseconds / 1000000).isToday()
}
