package com.jula1717.welly.domain.model

data class HabitFrequency(
    val times: Int,
    val periodLength: Int,
    val periodUnit: HabitPeriodUnit,
) {
    val windowDays: Int
        get() =
            periodLength *
                when (periodUnit) {
                    HabitPeriodUnit.Day -> 1
                    HabitPeriodUnit.Week -> DAYS_IN_WEEK
                    HabitPeriodUnit.Month -> DAYS_IN_MONTH
                }

    val isRolling: Boolean get() = windowDays > 1

    companion object {
        const val DAYS_IN_WEEK = 7
        const val DAYS_IN_MONTH = 30

        val Daily = HabitFrequency(times = 1, periodLength = 1, periodUnit = HabitPeriodUnit.Day)
        val Weekly = HabitFrequency(times = 1, periodLength = 1, periodUnit = HabitPeriodUnit.Week)
    }
}
