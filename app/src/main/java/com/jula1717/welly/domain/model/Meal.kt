package com.jula1717.welly.domain.model

import java.time.LocalDateTime

data class Meal(
    val id: Long = 0L,
    val dateTime: LocalDateTime,
    val type: MealType,
    val description: String,
    val macros: MealMacros,
)
