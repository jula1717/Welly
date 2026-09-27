package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.repository.WorkoutRepository
import java.time.LocalDateTime
import javax.inject.Inject

class AddWorkoutUseCase
    @Inject
    constructor(
        private val repository: WorkoutRepository,
    ) {
        suspend operator fun invoke(
            activityId: Long,
            dateTime: LocalDateTime,
        ) = repository.addWorkout(activityId, dateTime)
    }
