package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.withWorkoutGoal
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHabitUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val observeWorkoutGoal: ObserveWorkoutGoalUseCase,
    ) {
        operator fun invoke(id: Long): Flow<Habit?> =
            combine(repository.getHabit(id), observeWorkoutGoal()) { habit, workoutGoal ->
                habit?.withWorkoutGoal(workoutGoal)
            }
    }
