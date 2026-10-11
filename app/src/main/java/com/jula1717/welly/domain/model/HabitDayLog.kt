package com.jula1717.welly.domain.model

/** What happened on a single day for a habit: how many times it was done, plus any habit-specific [detail]. */
data class HabitDayLog(
    val count: Int,
    val detail: HabitDayDetail = HabitDayDetail.None,
)
