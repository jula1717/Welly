package com.jula1717.welly.presentation.profile

import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

internal const val MAX_AGE_YEARS = 120
private const val MONTHS_IN_YEAR = 12

internal fun birthYearRange(maxDate: LocalDate): IntRange = (maxDate.year - MAX_AGE_YEARS)..maxDate.year

/** Latest selectable month for [year], accounting for [maxDate] in the final year. */
internal fun maxBirthMonth(
    year: Int,
    maxDate: LocalDate,
): Int = if (year >= maxDate.year) maxDate.monthValue else MONTHS_IN_YEAR

/** Latest selectable day for [year]/[month], accounting for month length, leap years and [maxDate]. */
internal fun maxBirthDay(
    year: Int,
    month: Int,
    maxDate: LocalDate,
): Int {
    val monthLength = YearMonth.of(year, month).lengthOfMonth()
    return if (year >= maxDate.year && month >= maxDate.monthValue) {
        minOf(monthLength, maxDate.dayOfMonth)
    } else {
        monthLength
    }
}

/**
 * Clamps a `(year, month, day)` triple to a real date no later than [maxDate], adjusting only
 * the less-significant fields: year is bounded first, then month to that year, then day to that
 * month. Every result is a valid [LocalDate], so the picker never produces an impossible date,
 * and when a superior selection forces a change the cascade is always "clamp downwards".
 */
internal fun clampBirthDate(
    year: Int,
    month: Int,
    day: Int,
    maxDate: LocalDate,
): LocalDate {
    val years = birthYearRange(maxDate)
    val boundedYear = year.coerceIn(years.first, years.last)
    val boundedMonth = month.coerceIn(1, maxBirthMonth(boundedYear, maxDate))
    val boundedDay = day.coerceIn(1, maxBirthDay(boundedYear, boundedMonth, maxDate))
    return LocalDate.of(boundedYear, boundedMonth, boundedDay)
}

internal fun birthMonthName(
    month: Int,
    locale: Locale,
): String {
    val name = Month.of(month).getDisplayName(TextStyle.FULL_STANDALONE, locale)
    return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }
}
