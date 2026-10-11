package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.repository.HabitRepository
import javax.inject.Inject

class DeleteHabitUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        /** Deletes a custom habit with its history; built-in habits are refused, unknown ids are ignored. */
        suspend operator fun invoke(id: Long) {
            val habit = repository.findHabit(id) ?: return
            require(!habit.isBuiltIn) { "Built-in habits cannot be deleted" }
            repository.deleteHabit(id)
        }
    }
