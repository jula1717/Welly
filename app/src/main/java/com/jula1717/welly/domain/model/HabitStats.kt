package com.jula1717.welly.domain.model

import java.time.LocalDate

data class HabitStats(
    val currentStreak: Int,
    val longestStreak: Int,
    val rateLast7Days: Int?,
    val rateLast30Days: Int?,
    val monthlyCounts: List<HabitMonthCount>,
    val days: Map<LocalDate, HabitDayState>,
)
