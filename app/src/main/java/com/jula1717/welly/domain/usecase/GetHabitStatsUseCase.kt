package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.HabitStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class GetHabitStatsUseCase
    @Inject
    constructor(
        private val getHabit: GetHabitUseCase,
        private val getHabitLogs: GetHabitLogsUseCase,
        private val calculateStats: CalculateHabitStatsUseCase,
        private val clock: Clock,
    ) {
        @OptIn(ExperimentalCoroutinesApi::class)
        operator fun invoke(habitId: Long): Flow<HabitStats?> =
            getHabit(habitId).flatMapLatest { habit ->
                if (habit == null) {
                    flowOf(null)
                } else {
                    val today = LocalDate.now(clock)
                    getHabitLogs(habit, calculateStats.statsRange(habit, today)).map { logs ->
                        calculateStats(habit, logs, today)
                    }
                }
            }
    }
