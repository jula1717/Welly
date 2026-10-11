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
    fun `multi-day periods are chained from the start date`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Day, 3), startDate = LocalDate.of(2026, 10, 1))

        assertEquals(LocalDate.of(2026, 10, 7)..LocalDate.of(2026, 10, 9), useCase(habit, LocalDate.of(2026, 10, 8)))
    }

    @Test
    fun `multi-day periods also cover days before the start date`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Day, 3), startDate = LocalDate.of(2026, 10, 10))

        assertEquals(LocalDate.of(2026, 10, 7)..LocalDate.of(2026, 10, 9), useCase(habit, LocalDate.of(2026, 10, 8)))
    }

    @Test
    fun `weekly period runs from monday to sunday`() {
        val period = useCase(testHabit(frequency = HabitFrequency.Weekly), HABIT_TODAY)

        assertEquals(LocalDate.of(2026, 10, 12)..LocalDate.of(2026, 10, 18), period)
    }

    @Test
    fun `two-week periods are chained from the week of the start date`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Week, 2), startDate = LocalDate.of(2026, 10, 14))

        assertEquals(LocalDate.of(2026, 10, 12)..LocalDate.of(2026, 10, 25), useCase(habit, HABIT_TODAY))
        assertEquals(LocalDate.of(2026, 10, 26)..LocalDate.of(2026, 11, 8), useCase(habit, LocalDate.of(2026, 10, 27)))
    }

    @Test
    fun `monthly period is the calendar month`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Month))

        assertEquals(LocalDate.of(2026, 10, 1)..LocalDate.of(2026, 10, 31), useCase(habit, HABIT_TODAY))
        assertEquals(LocalDate.of(2026, 2, 1)..LocalDate.of(2026, 2, 28), useCase(habit, LocalDate.of(2026, 2, 10)))
    }

    @Test
    fun `multi-month periods are chained from the month of the start date`() {
        val habit = testHabit(frequency = frequency(HabitPeriodUnit.Month, 3), startDate = LocalDate.of(2026, 10, 20))

        assertEquals(LocalDate.of(2026, 10, 1)..LocalDate.of(2026, 12, 31), useCase(habit, LocalDate.of(2026, 12, 31)))
        assertEquals(LocalDate.of(2027, 1, 1)..LocalDate.of(2027, 3, 31), useCase(habit, LocalDate.of(2027, 1, 1)))
    }
}
