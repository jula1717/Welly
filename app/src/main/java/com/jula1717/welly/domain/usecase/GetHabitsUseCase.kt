package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHabitsUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        operator fun invoke(): Flow<List<Habit>> =
            repository.getHabits().map { habits ->
                habits.sortedBy { !it.isBuiltIn }
            }
    }
