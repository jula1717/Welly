package com.jula1717.welly.presentation.habitstats

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class CalendarWeeksTest {
    @Test
    fun `pads the first week up to the first weekday and the last week to sunday`() {
        val weeks = monthWeeks(YearMonth.of(2026, 10))

        assertEquals(5, weeks.size)
        assertTrue(weeks.all { it.size == 7 })
        assertEquals(listOf(null, null, null), weeks.first().take(3))
        assertEquals(LocalDate.of(2026, 10, 1), weeks.first()[3])
        assertEquals(LocalDate.of(2026, 10, 31), weeks.last()[5])
        assertNull(weeks.last()[6])
    }

    @Test
    fun `lists every day of the month once and in order`() {
        val days = monthWeeks(YearMonth.of(2026, 10)).flatten().filterNotNull()

        assertEquals((1..31).map { LocalDate.of(2026, 10, it) }, days)
    }

    @Test
    fun `a month that starts on monday and fits four weeks has no padding`() {
        val weeks = monthWeeks(YearMonth.of(2027, 2))

        assertEquals(4, weeks.size)
        assertTrue(weeks.flatten().none { it == null })
    }
}
