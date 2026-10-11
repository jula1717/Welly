package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.repository.HabitRepository
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class EnsureBuiltInHabitsUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke() {
            val habits = repository.findHabits()
            val existing = habits.mapNotNull { it.builtIn }.toSet()
            val today = LocalDate.now(clock)
            BuiltInHabit.entries
                .filterNot { it in existing }
                .forEach { builtIn -> repository.addHabit(defaultHabit(builtIn, today)) }
        }

        private fun defaultHabit(
            builtIn: BuiltInHabit,
            today: LocalDate,
        ): Habit =
            Habit(
                builtIn = builtIn,
                name = null,
                points = DEFAULT_POINTS,
                frequency = if (builtIn == BuiltInHabit.Workouts) HabitFrequency.Weekly else HabitFrequency.Daily,
                startDate = today,
            )

        private companion object {
            const val DEFAULT_POINTS = 10
        }
    }
