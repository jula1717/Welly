package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class CalculateHabitStatsUseCaseTest {
    private val getPeriod = GetHabitPeriodUseCase()
    private val useCase = CalculateHabitStatsUseCase(getPeriod, CalculateHabitDayStatesUseCase(getPeriod))

    private fun day(dayOfMonth: Int) = LocalDate.of(2026, 10, dayOfMonth)

    private fun days(range: IntRange) = range.map { day(it) }.toTypedArray()

    private val twicePerWeek = HabitFrequency(times = 2, periodLength = 1, periodUnit = HabitPeriodUnit.Week)

    @Test
    fun `current streak ignores a today that is still open`() {
        val stats = useCase(testHabit(), logsOn(*days(11..14)), HABIT_TODAY)

        assertEquals(4, stats.currentStreak)
    }

    @Test
    fun `current streak includes today once it is done`() {
        val stats = useCase(testHabit(), logsOn(*days(11..15)), HABIT_TODAY)

        assertEquals(5, stats.currentStreak)
    }

    @Test
    fun `a missed day breaks the current streak but not the longest one`() {
        val stats = useCase(testHabit(), logsOn(*days(1..5), *days(12..14)), HABIT_TODAY)

        assertEquals(3, stats.currentStreak)
        assertEquals(5, stats.longestStreak)
    }

    @Test
    fun `streak is zero when yesterday was missed`() {
        val stats = useCase(testHabit(), logsOn(*days(1..12)), HABIT_TODAY)

        assertEquals(0, stats.currentStreak)
        assertEquals(12, stats.longestStreak)
    }

    @Test
    fun `rates count finished days in the window`() {
        val stats = useCase(testHabit(), logsOn(*days(12..14)), HABIT_TODAY)

        assertEquals(50, stats.rateLast7Days)
        assertEquals(21, stats.rateLast30Days)
    }

    @Test
    fun `rates and streaks are empty for a habit that has just started`() {
        val stats = useCase(testHabit(startDate = HABIT_TODAY), emptyMap(), HABIT_TODAY)

        assertNull(stats.rateLast7Days)
        assertNull(stats.rateLast30Days)
        assertEquals(0, stats.currentStreak)
        assertEquals(0, stats.longestStreak)
    }

    @Test
    fun `weekly streak counts weeks that met the quota`() {
        val logs = logsOn(day(5), day(6), day(12), day(13))

        val stats = useCase(testHabit(frequency = twicePerWeek), logs, HABIT_TODAY)

        assertEquals(2, stats.currentStreak)
        assertEquals(2, stats.longestStreak)
        assertEquals(67, stats.rateLast30Days)
    }

    @Test
    fun `an open week that has not met the quota yet does not break the streak`() {
        val logs = logsOn(day(5), day(6), day(12))

        val stats = useCase(testHabit(frequency = twicePerWeek), logs, HABIT_TODAY)

        assertEquals(1, stats.currentStreak)
        assertEquals(1, stats.longestStreak)
    }

    @Test
    fun `several workouts in one week satisfy a weekly goal`() {
        val logs = mapOf(day(12) to HabitDayLog(count = 2))

        val stats = useCase(testHabit(frequency = twicePerWeek), logs, HABIT_TODAY)

        assertEquals(1, stats.currentStreak)
    }

    @Test
    fun `built-in habits are tracked over the chart range regardless of the start date`() {
        val habit = testHabit(builtIn = BuiltInHabit.Calories, startDate = HABIT_TODAY)

        val stats = useCase(habit, logsOn(*days(12..14)), HABIT_TODAY)

        assertEquals(3, stats.currentStreak)
    }

    @Test
    fun `monthly counts cover the last twelve months oldest first`() {
        val logs =
            mapOf(
                day(14) to HabitDayLog(count = 3),
                day(2) to HabitDayLog(count = 1),
                LocalDate.of(2026, 9, 20) to HabitDayLog(count = 2),
                LocalDate.of(2025, 11, 5) to HabitDayLog(count = 4),
            )

        val counts = useCase(testHabit(), logs, HABIT_TODAY).monthlyCounts

        assertEquals(12, counts.size)
        assertEquals(YearMonth.of(2025, 11), counts.first().month)
        assertEquals(4, counts.first().count)
        assertEquals(YearMonth.of(2026, 9), counts[10].month)
        assertEquals(2, counts[10].count)
        assertEquals(YearMonth.of(2026, 10), counts.last().month)
        assertEquals(4, counts.last().count)
    }

    @Test
    fun `stats range reaches back to the start of a long-running custom habit`() {
        val habit = testHabit(startDate = LocalDate.of(2024, 3, 3))

        assertEquals(LocalDate.of(2024, 3, 3)..HABIT_TODAY, useCase.statsRange(habit, HABIT_TODAY))
    }

    @Test
    fun `stats range reaches back to the start of the first weekly period`() {
        val habit = testHabit(builtIn = BuiltInHabit.Workouts, frequency = HabitFrequency.Weekly)
        assertEquals(LocalDate.of(2025, 10, 27)..HABIT_TODAY, useCase.statsRange(habit, HABIT_TODAY))
    }

    @Test
    fun `stats range of a built-in habit is the chart range`() {
        val habit = testHabit(builtIn = BuiltInHabit.Macros, startDate = LocalDate.of(2024, 3, 3))

        assertEquals(LocalDate.of(2025, 11, 1)..HABIT_TODAY, useCase.statsRange(habit, HABIT_TODAY))
    }
}
