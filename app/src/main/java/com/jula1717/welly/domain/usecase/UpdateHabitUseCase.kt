package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.repository.HabitRepository
import javax.inject.Inject

class UpdateHabitUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val validate: ValidateHabitUseCase,
    ) {
        /**
         * Saves only what the user may edit: name, points and the enabled flag, plus the frequency of custom
         * habits. Everything else (built-in identity, built-in frequency, start date) keeps its stored value.
         * A blank name resets a built-in habit to its default name; unknown ids are ignored. Throws
         * [IllegalArgumentException] for input that [ValidateHabitUseCase] rejects.
         */
        suspend operator fun invoke(habit: Habit) {
            val stored = repository.findHabit(habit.id) ?: return
            val name = habit.name?.trimEnd()
            val updated =
                stored.copy(
                    name = if (stored.isBuiltIn) name?.ifBlank { null } else name,
                    points = habit.points,
                    isEnabled = habit.isEnabled,
                    frequency = if (stored.isBuiltIn) stored.frequency else habit.frequency,
                )
            require(validate(updated)) { "Invalid habit: $updated" }
            repository.updateHabit(updated)
        }
    }
