package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHabitUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        operator fun invoke(id: Long): Flow<Habit?> = repository.getHabit(id)
    }
