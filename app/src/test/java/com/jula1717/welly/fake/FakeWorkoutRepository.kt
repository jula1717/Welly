package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate
import java.time.LocalDateTime

class FakeWorkoutRepository(
    private val workouts: List<Workout> = emptyList(),
) : WorkoutRepository {
    val added = mutableListOf<Pair<Long, LocalDateTime>>()

    override suspend fun addWorkout(
        activityId: Long,
        dateTime: LocalDateTime,
    ) {
        added += activityId to dateTime
    }

    override fun getWorkoutsInRange(range: ClosedRange<LocalDate>): Flow<List<Workout>> =
        flowOf(workouts.filter { it.dateTime.toLocalDate() in range })
}
