package com.jula1717.welly.presentation.today

import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.domain.model.Workout
import java.time.LocalTime

// TODO: once WorkoutEntry gets calories and description, move them up to TodayEntry.
sealed interface TodayEntry {
    val id: Long
    val time: LocalTime
    val key: String

    data class MealEntry(
        override val id: Long,
        override val time: LocalTime,
        val description: String,
        val calories: Int,
        val type: MealType,
    ) : TodayEntry {
        override val key: String get() = "meal-$id"
    }

    data class DrinkEntry(
        override val id: Long,
        override val time: LocalTime,
        val description: String,
        val calories: Int,
        val amountMl: Int,
    ) : TodayEntry {
        override val key: String get() = "drink-$id"
    }

    data class WorkoutEntry(
        override val id: Long,
        override val time: LocalTime,
        val activityName: String,
        val activityType: ActivityType,
    ) : TodayEntry {
        override val key: String get() = "workout-$id"
    }
}

internal fun nutritionEntries(
    meals: List<Meal>,
    drinks: List<Drink>,
): List<TodayEntry> = (meals.map { it.toTodayEntry() } + drinks.map { it.toTodayEntry() }).sortedByTime()

internal fun workoutEntries(workouts: List<Workout>): List<TodayEntry> =
    workouts.map { it.toTodayEntry() }.sortedByTime()

private fun List<TodayEntry>.sortedByTime(): List<TodayEntry> = sortedWith(compareBy({ it.time }, { it.key }))

internal fun Meal.toTodayEntry(): TodayEntry.MealEntry =
    TodayEntry.MealEntry(
        id = id,
        time = dateTime.toLocalTime(),
        description = description,
        calories = macros.calories,
        type = type,
    )

internal fun Drink.toTodayEntry(): TodayEntry.DrinkEntry =
    TodayEntry.DrinkEntry(
        id = id,
        time = dateTime.toLocalTime(),
        description = description,
        calories = macros?.calories ?: 0,
        amountMl = amountMl,
    )

internal fun Workout.toTodayEntry(): TodayEntry.WorkoutEntry =
    TodayEntry.WorkoutEntry(
        id = id,
        time = dateTime.toLocalTime(),
        activityName = activity.name,
        activityType = activity.type,
    )
