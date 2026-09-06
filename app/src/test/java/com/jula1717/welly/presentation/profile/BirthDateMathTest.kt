package com.jula1717.welly.presentation.profile

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class BirthDateMathTest {
    private val maxDate = LocalDate.of(2026, 9, 5)

    // option ranges

    @Test
    fun `year range spans MAX_AGE_YEARS back from the max date`() {
        val years = birthYearRange(maxDate)
        assertEquals(2026 - MAX_AGE_YEARS, years.first)
        assertEquals(2026, years.last)
    }

    @Test
    fun `february has 29 days in a leap year and 28 otherwise`() {
        assertEquals(29, maxBirthDay(year = 2020, month = 2, maxDate = maxDate))
        assertEquals(28, maxBirthDay(year = 2021, month = 2, maxDate = maxDate))
        assertEquals(29, maxBirthDay(year = 2000, month = 2, maxDate = maxDate))
        assertEquals(28, maxBirthDay(year = 1900, month = 2, maxDate = maxDate))
    }

    @Test
    fun `month length follows the calendar for 30 and 31 day months`() {
        assertEquals(31, maxBirthDay(year = 2000, month = 1, maxDate = maxDate))
        assertEquals(30, maxBirthDay(year = 2000, month = 4, maxDate = maxDate))
        assertEquals(31, maxBirthDay(year = 2000, month = 12, maxDate = maxDate))
    }

    // maxDate clamping

    @Test
    fun `max month is limited only in the final year`() {
        assertEquals(12, maxBirthMonth(year = 2025, maxDate = maxDate))
        assertEquals(9, maxBirthMonth(year = 2026, maxDate = maxDate))
    }

    @Test
    fun `max day is limited only in the final month of the final year`() {
        assertEquals(5, maxBirthDay(year = 2026, month = 9, maxDate = maxDate))
        assertEquals(31, maxBirthDay(year = 2026, month = 8, maxDate = maxDate))
        assertEquals(30, maxBirthDay(year = 2025, month = 9, maxDate = maxDate))
    }

    // clampBirthDate

    @Test
    fun `keeps a valid date untouched`() {
        assertEquals(LocalDate.of(1995, 6, 15), clampBirthDate(1995, 6, 15, maxDate))
    }

    @Test
    fun `clamps day 31 down to the length of a shorter month`() {
        assertEquals(LocalDate.of(1995, 4, 30), clampBirthDate(1995, 4, 31, maxDate))
        assertEquals(LocalDate.of(1995, 2, 28), clampBirthDate(1995, 2, 31, maxDate))
    }

    @Test
    fun `selecting the final year clamps month and day rather than jumping to maxDate`() {
        assertEquals(LocalDate.of(2026, 9, 5), clampBirthDate(2026, 12, 31, maxDate))
        assertEquals(LocalDate.of(2026, 8, 31), clampBirthDate(2026, 8, 31, maxDate))
    }

    @Test
    fun `bounds the year to the selectable range`() {
        val years = birthYearRange(maxDate)
        assertEquals(years.first, clampBirthDate(1800, 6, 15, maxDate).year)
        assertEquals(years.last, clampBirthDate(3000, 6, 15, maxDate).year)
    }

    // month naming

    @Test
    fun `month name is capitalised and localised`() {
        assertEquals(
            "January",
            birthMonthName(1, java.util.Locale.ENGLISH),
        )

        assertEquals(
            "Styczeń",
            birthMonthName(1, java.util.Locale.forLanguageTag("pl")),
        )
    }
}
