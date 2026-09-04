package com.jula1717.welly.presentation.today

import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealType
import java.time.LocalTime

sealed interface TodayEntry {
    val id: Long
    val time: LocalTime
    val description: String
    val calories: Int
    val key: String

    data class MealEntry(
        override val id: Long,
        override val time: LocalTime,
        override val description: String,
        override val calories: Int,
        val type: MealType,
    ) : TodayEntry {
        override val key: String get() = "meal-$id"
    }

    data class DrinkEntry(
        override val id: Long,
        override val time: LocalTime,
        override val description: String,
        override val calories: Int,
        val amountMl: Int,
    ) : TodayEntry {
        override val key: String get() = "drink-$id"
    }
}

internal fun todayEntries(
    meals: List<Meal>,
    drinks: List<Drink>,
): List<TodayEntry> =
    (meals.map { it.toTodayEntry() } + drinks.map { it.toTodayEntry() })
        .sortedWith(compareBy({ it.time }, { it.key }))

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
