package com.jula1717.welly.domain.model

import java.time.LocalDateTime

data class Drink(
    val id: Long = 0L,
    val dateTime: LocalDateTime,
    val amountMl: Int,
    val description: String = "",
    val macros: MealMacros? = null,
)
