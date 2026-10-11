package com.jula1717.welly.data.local.mapper

import com.jula1717.welly.data.local.entity.HabitCompletionEntity
import com.jula1717.welly.data.local.entity.HabitEntity
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitCompletion
import com.jula1717.welly.domain.model.HabitFrequency
import java.time.LocalDate

fun HabitEntity.toDomain(): Habit =
    Habit(
        id = id,
        builtIn = builtIn,
        name = name,
        points = points,
        frequency = HabitFrequency(times = times, periodLength = periodLength, periodUnit = periodUnit),
        isEnabled = isEnabled,
        startDate = LocalDate.ofEpochDay(startEpochDay),
    )

fun Habit.toEntity(): HabitEntity =
    HabitEntity(
        id = id,
        builtIn = builtIn,
        name = name,
        points = points,
        times = frequency.times,
        periodLength = frequency.periodLength,
        periodUnit = frequency.periodUnit,
        isEnabled = isEnabled,
        startEpochDay = startDate.toEpochDay(),
    )

fun HabitCompletionEntity.toDomain(): HabitCompletion =
    HabitCompletion(
        habitId = habitId,
        date = LocalDate.ofEpochDay(epochDay),
        count = count,
    )
