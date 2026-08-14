package com.jula1717.welly.data.local.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

internal fun LocalDate.toDayEpochSecondRange(): Pair<Long, Long> {
    val startOfDay = this.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
    val endOfDay = this.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)
    return startOfDay to endOfDay
}
