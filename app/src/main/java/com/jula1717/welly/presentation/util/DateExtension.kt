package com.jula1717.welly.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val UTC_ZONE = ZoneId.of("UTC")

internal val DATE_DISPLAY_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
internal val TIME_DISPLAY_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

internal fun LocalDate.toUtcEpochMillis(): Long =
    this.atStartOfDay(UTC_ZONE)
        .toInstant()
        .toEpochMilli()

internal fun Long.toUtcLocalDate(): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(UTC_ZONE)
        .toLocalDate()
