package com.jula1717.welly.data.local.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

internal fun ClosedRange<LocalDate>.toEpochSecondRange(): Pair<Long, Long> {
    val startOfFirstDay = start.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    val endOfLastDay = endInclusive.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)
    return startOfFirstDay to endOfLastDay
}
