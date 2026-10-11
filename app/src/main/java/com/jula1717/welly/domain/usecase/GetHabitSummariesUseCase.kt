package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDaySummary
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
            val range = getPeriod(habit, date).start..date
            return getHabitLogs(habit, range).map { logs ->
                val states = calculateDayStates(habit, logs, range, LocalDate.now(clock))
                HabitDaySummary(
                    habit = habit,
                    state = states[date] ?: HabitDayState.Empty,
                    periodCount = logs.values.sumOf { it.count },
                )
            }
        }
    }
