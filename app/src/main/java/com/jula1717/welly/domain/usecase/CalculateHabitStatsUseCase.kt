package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitMonthCount
import com.jula1717.welly.domain.model.HabitRate
import com.jula1717.welly.domain.model.HabitStats
import com.jula1717.welly.domain.util.dates
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.ceil

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
            val finished = finishedDays(habit, logs, today)
            return HabitStats(
                currentStreak = finished.takeLastWhile { it.met }.size,
                longestStreak = longestRun(finished),
                rateLast7Days = rate(habit, logs, today, WEEK_DAYS),
                rateLast30Days = rate(habit, logs, today, MONTH_DAYS),
                monthlyCounts = monthlyCounts(logs, today),
                days = calculateDayStates(habit, logs, statsRange(habit, today), today),
                firstCompletion = logs.filterValues { it.count > 0 }.keys.minOrNull(),
            )
        }

        private fun finishedDays(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            today: LocalDate,
        ): List<DayResult> =
            (trackingStart(habit, today)..today)
                .dates()
                .map { day ->
                    val count = getPeriod(habit, day).dates().sumOf { logs[it]?.count ?: 0 }
                    DayResult(day = day, met = count >= habit.frequency.times)
                }.filterNot { it.day == today && !it.met }
                .toList()

        private fun longestRun(days: List<DayResult>): Int {
            var longest = 0
            var current = 0
            days.forEach { result ->
                current = if (result.met) current + 1 else 0
                longest = maxOf(longest, current)
            }
            return longest
        }

        private fun rate(
            habit: Habit,
            logs: Map<LocalDate, HabitDayLog>,
            today: LocalDate,
            windowDays: Int,
        ): HabitRate? {
            val from = maxOf(today.minusDays(windowDays - 1L), trackingStart(habit, today))
            val doneToday = (logs[today]?.count ?: 0) > 0
            val to = if (doneToday) today else today.minusDays(1)
            if (to < from) return null

            val days = ChronoUnit.DAYS.between(from, to) + 1
            val expected = ceil(habit.frequency.times * days / habit.frequency.windowDays.toDouble()).toInt()
            val done = (from..to).dates().sumOf { logs[it]?.count ?: 0 }
            return HabitRate(met = minOf(done, expected), total = expected)
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

        private data class DayResult(
            val day: LocalDate,
            val met: Boolean,
        )

        private companion object {
            const val WEEK_DAYS = 7
            const val MONTH_DAYS = 30
            const val CHART_MONTHS = 12
        }
    }
