 package com.jula1717.welly.presentation.habitstats

import java.time.LocalDate
import java.time.YearMonth

private const val DAYS_IN_WEEK = 7

internal fun monthWeeks(month: YearMonth): List<List<LocalDate?>> {
    val leadingBlanks = month.atDay(1).dayOfWeek.value - 1
    val cells = List<LocalDate?>(leadingBlanks) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
    return cells.chunked(DAYS_IN_WEEK).map { week -> week + List(DAYS_IN_WEEK - week.size) { null } }
}
