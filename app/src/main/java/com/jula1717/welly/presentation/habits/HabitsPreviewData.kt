package com.jula1717.welly.presentation.habits

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitDaySummary
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import java.time.LocalDate

private val PREVIEW_START = LocalDate.of(2026, 1, 1)

internal val PreviewDate: LocalDate = LocalDate.of(2026, 8, 9)

private fun previewHabit(
    id: Long,
    name: String?,
    builtIn: BuiltInHabit? = null,
    frequency: HabitFrequency = HabitFrequency.Daily,
) = Habit(
    id = id,
    builtIn = builtIn,
    name = name,
    points = 10,
    frequency = frequency,
    startDate = PREVIEW_START,
)

internal val PreviewHabitSummaries: List<HabitDaySummary> =
    listOf(
        HabitDaySummary(
            habit = previewHabit(1, null, BuiltInHabit.Calories),
            state = HabitDayState(HabitDayStatus.Done, count = 1),
            periodCount = 1,
            currentStreak = 12,
        ),
        HabitDaySummary(
            habit = previewHabit(2, null, BuiltInHabit.Workouts, HabitFrequency(3, 1, HabitPeriodUnit.Week)),
            state = HabitDayState(HabitDayStatus.Covered),
            periodCount = 3,
            currentStreak = 4,
        ),
        HabitDaySummary(
            habit = previewHabit(3, "Brushing teeth", frequency = HabitFrequency(2, 1, HabitPeriodUnit.Day)),
            state = HabitDayState(HabitDayStatus.None, count = 1),
            periodCount = 1,
            currentStreak = 34,
        ),
        HabitDaySummary(
            habit = previewHabit(4, "Reading before bed"),
            state = HabitDayState(HabitDayStatus.None),
            periodCount = 0,
            currentStreak = 0,
        ),
        HabitDaySummary(
            habit = previewHabit(5, "Gym", frequency = HabitFrequency(2, 1, HabitPeriodUnit.Week)),
            state = HabitDayState(HabitDayStatus.Done, count = 1),
            periodCount = 1,
            currentStreak = 21,
        ),
    )
