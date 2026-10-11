package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.repository.HabitRepository
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class SetHabitCompletionCountUseCase
    @Inject
    constructor(
        private val repository: HabitRepository,
        private val clock: Clock,
    ) {
        /**
         * Manual check-in for a custom habit; a count of zero or less clears the day. Built-in habits are
         * refused (they are derived from logged data), and unknown ids are ignored.
         */
        suspend operator fun invoke(
            habitId: Long,
            date: LocalDate,
            count: Int,
        ) {
            val habit = repository.findHabit(habitId) ?: return
            require(habit.canCheckIn) { "Built-in habits cannot be checked in manually" }
            require(date <= LocalDate.now(clock)) { "Cannot check in a habit for a future day" }
            repository.setCompletionCount(habitId, date, count.coerceAtLeast(0))
        }
    }
