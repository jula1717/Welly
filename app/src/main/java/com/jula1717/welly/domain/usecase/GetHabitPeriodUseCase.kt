package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Habit
import java.time.LocalDate
import javax.inject.Inject

class GetHabitPeriodUseCase
    @Inject
    constructor() {
        operator fun invoke(
            habit: Habit,
            date: LocalDate,
        ): ClosedRange<LocalDate> = date.minusDays(habit.frequency.windowDays - 1L)..date
    }
