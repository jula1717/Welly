package com.jula1717.welly.domain.util

import java.time.LocalDate

fun ClosedRange<LocalDate>.dates(): Sequence<LocalDate> =
    generateSequence(start.takeIf { it <= endInclusive }) { day ->
        day.plusDays(1).takeIf { it <= endInclusive }
    }
