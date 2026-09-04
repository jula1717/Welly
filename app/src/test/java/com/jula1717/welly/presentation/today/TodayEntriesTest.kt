package com.jula1717.welly.presentation.today

import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class TodayEntriesTest {
    private val day = LocalDate.of(2026, 9, 4)

    @Test
    fun `entries from meals and drinks are interleaved and ordered by time`() {
        val meals = listOf(
            meal(LocalTime.of(8, 0), "scrumble eggs"),
            meal(LocalTime.of(19, 30), "pizza"),
        )
        val drinks = listOf(
            drink(LocalTime.of(7, 15), "water"),
            drink(LocalTime.of(13, 0), "coffee"),
        )

        val result = todayEntries(meals, drinks)

        assertEquals(
            listOf("water", "scrumble eggs", "coffee", "pizza"),
            result.map { it.description },
        )
        assertEquals(
            listOf(
                LocalTime.of(7, 15),
                LocalTime.of(8, 0),
                LocalTime.of(13, 0),
                LocalTime.of(19, 30),
            ),
            result.map { it.time },
        )
    }

    @Test
    fun `empty meals and drinks produce no entries`() {
        assertEquals(emptyList<TodayEntry>(), todayEntries(emptyList(), emptyList()))
    }

    private fun meal(
        time: LocalTime,
        description: String,
    ) = Meal(
        dateTime = LocalDateTime.of(day, time),
        type = MealType.Lunch,
        description = description,
        macros = MealMacros(calories = 300, protein = 10, carbs = 40, fat = 8, fiber = 4),
    )

    private fun drink(
        time: LocalTime,
        description: String,
    ) = Drink(
        dateTime = LocalDateTime.of(day, time),
        amountMl = 250,
        description = description,
        macros = null,
    )
}
