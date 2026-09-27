package com.jula1717.welly.data.local.mapper

import com.jula1717.welly.data.local.relation.WorkoutWithActivity
import com.jula1717.welly.domain.model.Workout
import java.time.LocalDateTime
import java.time.ZoneOffset

fun WorkoutWithActivity.toDomain(): Workout =
    Workout(
        id = workout.id,
        activity = activity.toDomain(),
        dateTime = LocalDateTime.ofEpochSecond(workout.dateTimeEpochSecond, 0, ZoneOffset.UTC),
    )
