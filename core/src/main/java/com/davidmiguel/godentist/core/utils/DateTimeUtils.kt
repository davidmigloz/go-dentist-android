package com.davidmiguel.godentist.core.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun nowToEpochMilliUtc(): Long = LocalDateTime.now()
    .atZone(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
    .toInstant()
    .toEpochMilli()

fun LocalDateTime.formatDMY(): String = format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

fun Long.formatDMY(): String {
    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    return date.formatDMY()
}
