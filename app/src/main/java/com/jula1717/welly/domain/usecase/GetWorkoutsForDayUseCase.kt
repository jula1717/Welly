package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetWorkoutsForDayUseCase
    @Inject
    constructor(
        private val repository: WorkoutRepository,
    ) {
        operator fun invoke(date: LocalDate): Flow<List<Workout>> = repository.getWorkoutsForDay(date)
    }
