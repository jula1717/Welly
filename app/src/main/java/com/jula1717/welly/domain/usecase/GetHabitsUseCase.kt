package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.withWorkoutGoal
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetHabitsUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val observeWorkoutGoal: ObserveWorkoutGoalUseCase,
    ) {
        operator fun invoke(): Flow<List<Habit>> =
            combine(repository.getHabits(), observeWorkoutGoal()) { habits, workoutGoal ->
                habits.map { it.withWorkoutGoal(workoutGoal) }.sortedBy { !it.isBuiltIn }
            }
    }
