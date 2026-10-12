package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import javax.inject.Inject

class ValidateHabitUseCase
    @Inject
    constructor() {
        operator fun invoke(habit: Habit): Boolean =
            invoke(
                name = habit.name,
                points = habit.points,
                frequency = habit.frequency,
                isBuiltIn = habit.isBuiltIn,
            )

      operator fun invoke(
            name: String?,
            points: Int,
            frequency: HabitFrequency,
            isBuiltIn: Boolean,
        ): Boolean =
            isNameValid(name, isBuiltIn) &&
                points >= 0 &&
                frequency.times >= 1 &&
                frequency.periodLength >= 1 &&
                (isBuiltIn || isQuotaAttainable(frequency))

        private fun isNameValid(
            name: String?,
            isBuiltIn: Boolean,
        ): Boolean {
            if (name.isNullOrBlank()) return isBuiltIn
            return !name.first().isWhitespace()
        }

        private fun isQuotaAttainable(frequency: HabitFrequency): Boolean =
            !frequency.isRolling || frequency.times <= frequency.windowDays
    }
