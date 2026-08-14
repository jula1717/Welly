package com.jula1717.welly.data.local.mapper

import com.jula1717.welly.data.local.entity.DrinkEntity
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.MealMacros
import java.time.LocalDateTime
import java.time.ZoneOffset

fun DrinkEntity.toDomain(): Drink =
    Drink(
        id = id,
        dateTime = LocalDateTime.ofEpochSecond(dateTimeEpochSecond, 0, ZoneOffset.UTC),
        amountMl = amountMl,
        description = description,
        macros = macrosOrNull(),
    )

private fun DrinkEntity.macrosOrNull(): MealMacros? {
    val calories = calories ?: return null
    val protein = protein ?: return null
    val carbs = carbs ?: return null
    val fat = fat ?: return null
    val fiber = fiber ?: return null
    return MealMacros(calories = calories, protein = protein, carbs = carbs, fat = fat, fiber = fiber)
}

fun Drink.toEntity(): DrinkEntity =
    DrinkEntity(
        id = id,
        dateTimeEpochSecond = dateTime.toEpochSecond(ZoneOffset.UTC),
        amountMl = amountMl,
        description = description,
        calories = macros?.calories,
        protein = macros?.protein,
        carbs = macros?.carbs,
        fat = macros?.fat,
        fiber = macros?.fiber,
    )
