package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.util.dates
import java.time.LocalDate
import javax.inject.Inject

class CalculateHabitDayStatesUseCase
    @Inject
    constructor(
        private val getPeriod: GetHabitPeriodUseCase,
    ) {
        operator fun invoke(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            range: ClosedRange<LocalDate>,
            today: LocalDate,
        ): Map<LocalDate, HabitDayState> {
            val lastDay = minOf(range.endInclusive, today)
            return (range.start..lastDay).dates().associateWith { date -> stateOf(habit, logs, date) }
        }

        private fun stateOf(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            date: LocalDate,
        ): HabitDayState {
            val log = logs[date]
            val count = log?.count ?: 0
            val period = getPeriod(habit, date)
            val times = habit.frequency.times
            val status =
                when {
                    period.start == period.endInclusive && count >= times -> HabitDayStatus.Done
                    period.start == period.endInclusive -> HabitDayStatus.None
                    count > 0 -> HabitDayStatus.Done
                    isCovered(logs, period, times, date) -> HabitDayStatus.Covered
                    else -> HabitDayStatus.None
                }
            return HabitDayState(status = status, count = count, detail = log?.detail ?: HabitDayDetail.None)
        }

        private fun isCovered(
            logs: Map<LocalDate, HabitDayLog>,
            period: ClosedRange<LocalDate>,
            times: Int,
            date: LocalDate,
        ): Boolean {
            var total = 0
            val quotaMetOn =
                period.dates().firstOrNull { day ->
                    total += logs[day]?.count ?: 0
                    total >= times
                }
            return quotaMetOn != null && date > quotaMetOn
        }
    }
