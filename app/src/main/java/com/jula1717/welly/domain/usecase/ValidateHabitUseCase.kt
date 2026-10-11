package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import javax.inject.Inject

class ValidateHabitUseCase
    @Inject
    constructor() {
        operator fun invoke(habit: Habit): Boolean =
            isNameValid(habit) &&
                habit.points >= 0 &&
                habit.frequency.times >= 1 &&
                habit.frequency.periodLength >= 1

        private fun isNameValid(habit: Habit): Boolean {
            val name = habit.name
            if (name.isNullOrBlank()) return habit.isBuiltIn
            return !name.first().isWhitespace()
        }
    }
