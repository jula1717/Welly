package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitMonthCount
import com.jula1717.welly.domain.model.HabitStats
import com.jula1717.welly.domain.util.dates
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateHabitStatsUseCase
    @Inject
    constructor(
        private val getPeriod: GetHabitPeriodUseCase,
        private val calculateDayStates: CalculateHabitDayStatesUseCase,
    ) {
        fun statsRange(
            habit: Habit,
            today: LocalDate,
        ): ClosedRange<LocalDate> {
            val earliest = minOf(chartStart(today), trackingStart(habit, today))
            return getPeriod(habit, earliest).start..today
        }

        operator fun invoke(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            today: LocalDate,
        ): HabitStats {
            val finished = finishedPeriods(habit, logs, today)
            return HabitStats(
                currentStreak = finished.takeLastWhile { it.met }.size,
                longestStreak = longestRun(finished),
                rateLast7Days = rate(finished, today, WEEK_DAYS),
                rateLast30Days = rate(finished, today, MONTH_DAYS),
                monthlyCounts = monthlyCounts(logs, today),
                days = calculateDayStates(habit, logs, statsRange(habit, today), today),
            )
        }

        private fun finishedPeriods(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            today: LocalDate,
        ): List<PeriodResult> =
            generateSequence(getPeriod(habit, trackingStart(habit, today))) { period ->
                getPeriod(habit, period.endInclusive.plusDays(1))
            }.takeWhile { it.start <= today }
                .map { period ->
                    val total = period.dates().sumOf { logs[it]?.count ?: 0 }
                    PeriodResult(period = period, met = total >= habit.frequency.times, inProgress = today in period)
                }.filterNot { it.inProgress && !it.met }
                .toList()

        private fun longestRun(periods: List<PeriodResult>): Int {
            var longest = 0
            var current = 0
            periods.forEach { result ->
                current = if (result.met) current + 1 else 0
                longest = maxOf(longest, current)
            }
            return longest
        }

        private fun rate(
            periods: List<PeriodResult>,
            today: LocalDate,
            days: Int,
        ): Int? {
            val windowStart = today.minusDays(days - 1L)
            val inWindow = periods.filter { it.period.endInclusive >= windowStart }
            if (inWindow.isEmpty()) return null
            return (inWindow.count { it.met } * PERCENT / inWindow.size.toDouble()).roundToInt()
        }

        private fun monthlyCounts(
            logs: Map<LocalDate, HabitDayLog>,
            today: LocalDate,
        ): List<HabitMonthCount> {
            val countsByMonth =
                logs.entries
                    .groupBy({ YearMonth.from(it.key) }, { it.value.count })
                    .mapValues { it.value.sum() }
            return (CHART_MONTHS - 1 downTo 0).map { monthsAgo ->
                val month = YearMonth.from(today).minusMonths(monthsAgo.toLong())
                HabitMonthCount(month = month, count = countsByMonth[month] ?: 0)
            }
        }

        private fun chartStart(today: LocalDate): LocalDate =
            YearMonth.from(today).minusMonths(CHART_MONTHS - 1L).atDay(1)

        private fun trackingStart(
            habit: Habit,
            today: LocalDate,
        ): LocalDate = if (habit.isBuiltIn) chartStart(today) else habit.startDate

        private data class PeriodResult(
            val period: ClosedRange<LocalDate>,
            val met: Boolean,
            val inProgress: Boolean,
        )

        private companion object {
            const val WEEK_DAYS = 7
            const val MONTH_DAYS = 30
            const val CHART_MONTHS = 12
            const val PERCENT = 100
        }
    }
