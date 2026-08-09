package com.jula1717.welly.data.local.mapper

import com.jula1717.welly.data.local.entity.MealEntity
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import java.time.LocalDateTime
import java.time.ZoneOffset

fun MealEntity.toDomain(): Meal =
    Meal(
        id = id,
        dateTime = LocalDateTime.ofEpochSecond(dateTimeEpochSecond, 0, ZoneOffset.UTC),
        type = type,
        description = description,
        macros = MealMacros(
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat,
            fiber = fiber,
        ),
    )

fun Meal.toEntity(): MealEntity =
    MealEntity(
        id = id,
        dateTimeEpochSecond = dateTime.toEpochSecond(ZoneOffset.UTC),
        type = type,
        description = description,
        calories = macros.calories,
        protein = macros.protein,
        carbs = macros.carbs,
        fat = macros.fat,
        fiber = macros.fiber,
    )
