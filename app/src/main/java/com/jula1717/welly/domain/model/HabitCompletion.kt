package com.jula1717.welly.domain.model

import java.time.LocalDate

data class HabitCompletion(
    val habitId: Long,
    val date: LocalDate,
    val count: Int,
)
