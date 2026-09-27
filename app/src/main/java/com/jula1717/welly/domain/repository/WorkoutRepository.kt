package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.Workout
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

interface WorkoutRepository {
    suspend fun addWorkout(
        activityId: Long,
        dateTime: LocalDateTime,
    )

    fun getWorkoutsForDay(date: LocalDate): Flow<List<Workout>>
}
