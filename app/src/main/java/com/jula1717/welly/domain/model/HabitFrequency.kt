package com.jula1717.welly.domain.model

data class HabitFrequency(
    val times: Int,
    val periodLength: Int,
    val periodUnit: HabitPeriodUnit,
) {
    companion object {
        val Daily = HabitFrequency(times = 1, periodLength = 1, periodUnit = HabitPeriodUnit.Day)
        val Weekly = HabitFrequency(times = 1, periodLength = 1, periodUnit = HabitPeriodUnit.Week)
    }
}
