package com.jula1717.welly.presentation.today

import java.time.LocalDate

/** Whether the user may step forward from [date]. */
internal fun canGoToNextDay(
    date: LocalDate,
    today: LocalDate,
): Boolean = date.isBefore(today)

/** One day after [current], clamped so it never lands past [today]. */
internal fun nextDay(
    current: LocalDate,
    today: LocalDate,
): LocalDate = if (current.isBefore(today)) current.plusDays(1) else current

/** [date] if it is not past [today], otherwise [today] itself. */
internal fun coerceNotFuture(
    date: LocalDate,
    today: LocalDate,
): LocalDate = if (date.isAfter(today)) today else date
