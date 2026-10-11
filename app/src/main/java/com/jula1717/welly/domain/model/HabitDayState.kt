package com.jula1717.welly.domain.model

data class HabitDayState(
    val status: HabitDayStatus,
    val count: Int = 0,
    val detail: HabitDayDetail = HabitDayDetail.None,
) {
    companion object {
        val Empty = HabitDayState(status = HabitDayStatus.None)
    }
}
