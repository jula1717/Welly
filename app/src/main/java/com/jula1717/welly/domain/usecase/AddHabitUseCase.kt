package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.repository.HabitRepository
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

/** Adds a custom habit; built-in habits are created only by [EnsureBuiltInHabitsUseCase]. */
class AddHabitUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val validate: ValidateHabitUseCase,
        private val clock: Clock,
    ) {
        /** Throws [IllegalArgumentException] for input that [ValidateHabitUseCase] rejects. */
        suspend operator fun invoke(
            name: String,
            points: Int,
            frequency: HabitFrequency,
        ) {
            val habit =
                Habit(
                    name = name.trimEnd(),
                    points = points,
                    frequency = frequency,
                    startDate = LocalDate.now(clock),
                )
            require(validate(habit)) { "Invalid habit: $habit" }
            repository.addHabit(habit)
        }
    }
