package com.jula1717.welly.presentation.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

private val UTC_ZONE = ZoneId.of("UTC")

internal fun LocalDate.toUtcEpochMillis(): Long =
    this.atStartOfDay(UTC_ZONE)
        .toInstant()
        .toEpochMilli()

internal fun Long.toUtcLocalDate(): LocalDate =
    Instant.ofEpochMilli(this)
        .atZone(UTC_ZONE)
        .toLocalDate()
