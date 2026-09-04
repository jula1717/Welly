package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.DailyIntakeTotals
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CalculateDailyIntakeTotalsUseCaseTest {
    private val useCase = CalculateDailyIntakeTotalsUseCase()

    @Test
    fun `empty day totals to EMPTY`() {
        assertEquals(DailyIntakeTotals.EMPTY, useCase(meals = emptyList(), drinks = emptyList()))
    }

    @Test
    fun `sums macros across meals and caloric drinks`() {
        val meals = listOf(
            meal(macros = MealMacros(calories = 500, protein = 30, carbs = 60, fat = 15, fiber = 8)),
            meal(macros = MealMacros(calories = 700, protein = 40, carbs = 80, fat = 20, fiber = 12)),
        )
        val drinks = listOf(
            drink(amountMl = 250, macros = MealMacros(calories = 120, protein = 8, carbs = 12, fat = 4, fiber = 0)),
        )

        val totals = useCase(meals, drinks)

        assertEquals(1320, totals.calories)
        assertEquals(78, totals.protein)
        assertEquals(152, totals.carbs)
        assertEquals(39, totals.fat)
        assertEquals(20, totals.fiber)
    }

    @Test
    fun `drinks without macros add nothing to macro totals`() {
        val meals = listOf(
            meal(macros = MealMacros(calories = 400, protein = 25, carbs = 45, fat = 12, fiber = 6)),
        )
        val drinks = listOf(
            drink(amountMl = 500, macros = null),
            drink(amountMl = 300, macros = null),
        )

        val totals = useCase(meals, drinks)

        assertEquals(400, totals.calories)
        assertEquals(25, totals.protein)
        assertEquals(45, totals.carbs)
        assertEquals(12, totals.fat)
        assertEquals(6, totals.fiber)
    }

    @Test
    fun `hydration is the sum of every drink volume regardless of macros`() {
        val drinks = listOf(
            drink(amountMl = 500, macros = null),
            drink(amountMl = 250, macros = MealMacros(calories = 90, protein = 0, carbs = 22, fat = 0, fiber = 0)),
            drink(amountMl = 330, macros = null),
        )

        val totals = useCase(meals = emptyList(), drinks = drinks)

        assertEquals(1080, totals.hydrationMl)
        assertEquals(90, totals.calories)
    }

    @Test
    fun `meals do not contribute to hydration`() {
        val meals = listOf(
            meal(macros = MealMacros(calories = 600, protein = 35, carbs = 70, fat = 18, fiber = 9)),
        )

        assertEquals(0, useCase(meals, drinks = emptyList()).hydrationMl)
    }

    private fun meal(macros: MealMacros) =
        Meal(
            dateTime = LocalDateTime.of(2026, 9, 4, 12, 0),
            type = MealType.Lunch,
            description = "test meal",
            macros = macros,
        )

    private fun drink(
        amountMl: Int,
        macros: MealMacros?,
    ) = Drink(
        dateTime = LocalDateTime.of(2026, 9, 4, 15, 0),
        amountMl = amountMl,
        description = "test drink",
        macros = macros,
    )
}
