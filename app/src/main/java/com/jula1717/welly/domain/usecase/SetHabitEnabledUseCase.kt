package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.repository.HabitRepository
import javax.inject.Inject

/** Turns a habit on or off without touching anything else; unknown ids are ignored. */
class SetHabitEnabledUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
    ) {
        suspend operator fun invoke(
            habitId: Long,
            enabled: Boolean,
        ) {
            val habit = repository.findHabit(habitId) ?: return
            repository.updateHabit(habit.copy(isEnabled = enabled))
        }
    }
