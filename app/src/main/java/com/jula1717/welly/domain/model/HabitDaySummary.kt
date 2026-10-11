package com.jula1717.welly.domain.model

data class HabitDaySummary(
    val habit: Habit,
    val state: HabitDayState,
    val periodCount: Int,
)
