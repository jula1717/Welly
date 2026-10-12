package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class GetHabitPeriodUseCaseTest {
    private val useCase = GetHabitPeriodUseCase()

    private fun frequency(
        unit: HabitPeriodUnit,
        length: Int = 1,
    ) = HabitFrequency(times = 1, periodLength = length, periodUnit = unit)

    @Test
    fun `daily period is the day itself`() {
        val period = useCase(testHabit(), HABIT_TODAY)

        assertEquals(HABIT_TODAY..HABIT_TODAY, period)
    }

    @Test
    fun `a period counted in days is the window of days ending on the date`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Day, 3))

        assertEquals(LocalDate.of(2026, 10, 6)..LocalDate.of(2026, 10, 8), useCase(habit, LocalDate.of(2026, 10, 8)))
    }

    @Test
    fun `a week is the last seven days, not the calendar week`() {
        val period = useCase(testHabit(frequency = HabitFrequency.Weekly), HABIT_TODAY)

        assertEquals(LocalDate.of(2026, 10, 9)..HABIT_TODAY, period)
    }

    @Test
    fun `a week does not reset on monday`() {
        val habit = testHabit(frequency = HabitFrequency.Weekly)
        val monday = HABIT_MONDAY

        assertEquals(monday.minusDays(6)..monday, useCase(habit, monday))
    }

    @Test
    fun `a month is the last thirty days, not the calendar month`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Month))

        assertEquals(LocalDate.of(2026, 9, 16)..HABIT_TODAY, useCase(habit, HABIT_TODAY))
        assertEquals(LocalDate.of(2026, 10, 3)..LocalDate.of(2026, 11, 1), useCase(habit, LocalDate.of(2026, 11, 1)))
    }

    @Test
    fun `several weeks or months multiply the window`() {
        val twoWeeks = testHabit(frequency = frequency(HabitPeriodUnit.Week, 2))
        val twoMonths = testHabit(frequency = frequency(HabitPeriodUnit.Month, 2))

        assertEquals(HABIT_TODAY.minusDays(13)..HABIT_TODAY, useCase(twoWeeks, HABIT_TODAY))
        assertEquals(HABIT_TODAY.minusDays(59)..HABIT_TODAY, useCase(twoMonths, HABIT_TODAY))
    }

    @Test
    fun `the window does not depend on the start date`() {
        val early = testHabit(frequency = frequency(HabitPeriodUnit.Day, 5), startDate = LocalDate.of(2026, 1, 1))
        val late = testHabit(frequency = frequency(HabitPeriodUnit.Day, 5), startDate = LocalDate.of(2026, 10, 14))

        assertEquals(useCase(early, HABIT_TODAY), useCase(late, HABIT_TODAY))
        assertEquals(LocalDate.of(2026, 10, 11)..HABIT_TODAY, useCase(late, HABIT_TODAY))
    }
}
