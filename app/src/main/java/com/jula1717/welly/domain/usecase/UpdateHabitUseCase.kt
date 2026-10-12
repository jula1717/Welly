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
         * Saves only what the user may edit: for a custom habit its name, points, frequency and enabled flag, for
         * a built-in habit just the points and the enabled flag. Everything else (built-in identity, name and
         * frequency, start date) keeps its stored value; unknown ids are ignored. Throws
         * [IllegalArgumentException] for input that [ValidateHabitUseCase] rejects.
         */
        suspend operator fun invoke(habit: Habit) {
            val stored = repository.findHabit(habit.id) ?: return
            val updated =
                if (stored.isBuiltIn) {
                    stored.copy(points = habit.points, isEnabled = habit.isEnabled)
                } else {
                    stored.copy(
                        name = habit.name?.trimEnd(),
                        points = habit.points,
                        isEnabled = habit.isEnabled,
                        frequency = habit.frequency,
                    )
                }
            require(validate(updated)) { "Invalid habit: $updated" }
            repository.updateHabit(updated)
        }
    }
