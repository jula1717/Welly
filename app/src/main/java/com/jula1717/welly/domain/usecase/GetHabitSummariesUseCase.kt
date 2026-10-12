package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDaySummary
import com.jula1717.welly.domain.util.dates
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class GetHabitSummariesUseCase
    @Inject
    constructor(
        private val getHabits: GetHabitsUseCase,
        private val getHabitLogs: GetHabitLogsUseCase,
        private val getPeriod: GetHabitPeriodUseCase,
        private val calculateDayStates: CalculateHabitDayStatesUseCase,
        private val calculateStats: CalculateHabitStatsUseCase,
        private val clock: Clock,
    ) {
        @OptIn(ExperimentalCoroutinesApi::class)
        operator fun invoke(date: LocalDate): Flow<List<HabitDaySummary>> =
            getHabits().flatMapLatest { habits ->
                val enabled = habits.filter { it.isEnabled }
                if (enabled.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(enabled.map { summary(it, date) }) { it.toList() }
                }
            }

        private fun summary(
            habit: Habit,
            date: LocalDate,
        ): Flow<HabitDaySummary> {
            val today = LocalDate.now(clock)
            val periodUntilDate = getPeriod(habit, date).start..date
            val logsRange = minOf(calculateStats.statsRange(habit, today).start, periodUntilDate.start)..today
            return getHabitLogs(habit, logsRange).map { logs ->
                val states = calculateDayStates(habit, logs, periodUntilDate, today)
                HabitDaySummary(
                    habit = habit,
                    state = states[date] ?: HabitDayState.Empty,
                    periodCount = periodUntilDate.dates().sumOf { logs[it]?.count ?: 0 },
                    currentStreak = calculateStats(habit, logs, today).currentStreak,
                )
            }
        }
    }
