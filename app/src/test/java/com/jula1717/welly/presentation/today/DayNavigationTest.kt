package com.jula1717.welly.presentation.today

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DayNavigationTest {
    private val today = LocalDate.of(2026, 9, 4)

    @Test
    fun `canGoToNextDay is true for a past day`() {
        assertTrue(canGoToNextDay(date = today.minusDays(1), today = today))
    }

    @Test
    fun `canGoToNextDay is false for today`() {
        assertFalse(canGoToNextDay(date = today, today = today))
    }

    @Test
    fun `canGoToNextDay is false for a future day`() {
        assertFalse(canGoToNextDay(date = today.plusDays(1), today = today))
    }

    @Test
    fun `nextDay advances a past day by one`() {
        assertEquals(today.minusDays(2), nextDay(current = today.minusDays(3), today = today))
    }

    @Test
    fun `nextDay is clamped at today`() {
        assertEquals(today, nextDay(current = today, today = today))
    }

    @Test
    fun `coerceNotFuture keeps a past or present date`() {
        assertEquals(today.minusDays(5), coerceNotFuture(date = today.minusDays(5), today = today))
        assertEquals(today, coerceNotFuture(date = today, today = today))
    }

    @Test
    fun `coerceNotFuture snaps a future date to today`() {
        assertEquals(today, coerceNotFuture(date = today.plusDays(10), today = today))
    }
}
