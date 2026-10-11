package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitPeriodUnit
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class GetHabitPeriodUseCase
    @Inject
    constructor() {
        operator fun invoke(
            habit: Habit,
            date: LocalDate,
        ): ClosedRange<LocalDate> {
            val length = habit.frequency.periodLength.toLong()
            return when (habit.frequency.periodUnit) {
                HabitPeriodUnit.Day -> dayPeriod(habit.startDate, length, date)
                HabitPeriodUnit.Week -> weekPeriod(habit.startDate, length, date)
                HabitPeriodUnit.Month -> monthPeriod(habit.startDate, length, date)
            }
        }

        private fun dayPeriod(
            anchor: LocalDate,
            length: Long,
            date: LocalDate,
        ): ClosedRange<LocalDate> {
            val index = Math.floorDiv(ChronoUnit.DAYS.between(anchor, date), length)
            val start = anchor.plusDays(index * length)
            return start..start.plusDays(length - 1)
        }

        private fun weekPeriod(
            anchor: LocalDate,
            length: Long,
            date: LocalDate,
        ): ClosedRange<LocalDate> {
            val anchorWeekStart = anchor.startOfWeek()
            val index = Math.floorDiv(ChronoUnit.WEEKS.between(anchorWeekStart, date.startOfWeek()), length)
            val start = anchorWeekStart.plusWeeks(index * length)
            return start..start.plusWeeks(length).minusDays(1)
        }

        private fun monthPeriod(
            anchor: LocalDate,
            length: Long,
            date: LocalDate,
        ): ClosedRange<LocalDate> {
            val anchorMonth = YearMonth.from(anchor)
            val index = Math.floorDiv(ChronoUnit.MONTHS.between(anchorMonth, YearMonth.from(date)), length)
            val firstMonth = anchorMonth.plusMonths(index * length)
            return firstMonth.atDay(1)..firstMonth.plusMonths(length - 1).atEndOfMonth()
        }

        private fun LocalDate.startOfWeek(): LocalDate = with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }
