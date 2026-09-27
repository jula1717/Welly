package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.WorkoutDao
import com.jula1717.welly.data.local.entity.WorkoutEntity
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.util.toDayEpochSecondRange
import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class WorkoutRepositoryImpl
    @Inject
    constructor(
        private val workoutDao: WorkoutDao,
    ) : WorkoutRepository {
        override suspend fun addWorkout(
            activityId: Long,
            dateTime: LocalDateTime,
        ) {
            workoutDao.insert(
                WorkoutEntity(
                    activityId = activityId,
                    dateTimeEpochSecond = dateTime.toEpochSecond(ZoneOffset.UTC),
                ),
            )
        }

        override fun getWorkoutsForDay(date: LocalDate): Flow<List<Workout>> {
            val (startOfDay, endOfDay) = date.toDayEpochSecondRange()
            return workoutDao.getWorkoutsForDay(startOfDay, endOfDay).map { relations ->
                relations.map { it.toDomain() }
            }
        }
    }
