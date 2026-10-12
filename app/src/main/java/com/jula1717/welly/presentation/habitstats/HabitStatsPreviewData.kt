package com.jula1717.welly.presentation.habitstats

import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitMonthCount
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.domain.model.HabitRate
import com.jula1717.welly.domain.model.HabitStats
import java.time.LocalDate
import java.time.YearMonth

internal val PreviewToday: LocalDate = LocalDate.of(2026, 7, 20)

internal val PreviewHabit = Habit(
    id = 1,
    name = "Gym",
    points = 15,
    frequency = HabitFrequency(times = 2, periodLength = 1, periodUnit = HabitPeriodUnit.Week),
    startDate = LocalDate.of(2026, 1, 1),
)

private val PreviewDayStatuses = listOf(
    HabitDayStatus.Done,
    HabitDayStatus.Done,
    HabitDayStatus.Covered,
    HabitDayStatus.Covered,
    HabitDayStatus.None,
    HabitDayStatus.Done,
    HabitDayStatus.Covered,
    HabitDayStatus.None,
    HabitDayStatus.Done,
    HabitDayStatus.Done,
    HabitDayStatus.Covered,
    HabitDayStatus.None,
    HabitDayStatus.Done,
    HabitDayStatus.Covered,
    HabitDayStatus.Covered,
)

internal val PreviewCalendarDays: Map<LocalDate, HabitDayState> =
    PreviewDayStatuses
        .mapIndexed { index, status ->
            val detail = when (index) {
                1 -> HabitDayDetail.Workouts(setOf(ActivityType.Cardio, ActivityType.Strength))
                0 -> HabitDayDetail.Workouts(setOf(ActivityType.Strength))
                else -> HabitDayDetail.None
            }
            YearMonth.from(PreviewToday).atDay(index + 1) to HabitDayState(status, count = 1, detail = detail)
        }.toMap()

private val PreviewMonthlyValues = listOf(7, 9, 8, 6, 9, 8, 10, 9, 8, 7, 10, 2)

internal val PreviewStats = HabitStats(
    currentStreak = 21,
    longestStreak = 48,
    rateLast7Days = HabitRate(met = 2, total = 2),
    rateLast30Days = HabitRate(met = 7, total = 8),
    monthlyCounts = PreviewMonthlyValues.mapIndexed { index, count ->
        val monthsAgo = PreviewMonthlyValues.size - 1L - index
        HabitMonthCount(month = YearMonth.from(PreviewToday).minusMonths(monthsAgo), count = count)
    },
    days = PreviewCalendarDays,
)
