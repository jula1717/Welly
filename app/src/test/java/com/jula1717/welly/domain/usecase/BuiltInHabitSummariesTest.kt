package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitDaySummary
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class BuiltInHabitSummariesTest {
    private suspend fun summaryOf(
        builtIn: BuiltInHabit,
        meals: List<Meal> = emptyList(),
        drinks: List<Drink> = emptyList(),
    ): HabitDaySummary {
        val habits = FakeHabitRepository(listOf(testHabit(id = 1, builtIn = builtIn)))
        return getHabitSummariesUseCase(habits, meals = meals, drinks = drinks)(HABIT_TODAY).first().single()
    }

    private fun mealWith(calories: Int): Meal {
        val macros = MealMacros(calories = calories, protein = 0, carbs = 0, fat = 0, fiber = 0)
        return Meal(dateTime = HABIT_TODAY.atTime(12, 0), type = MealType.Lunch, description = "", macros = macros)
    }

    @Test
    fun `drinking well above the lower bound marks hydration as done today`() =
        runTest {
            val drinks = listOf(Drink(dateTime = HABIT_TODAY.atTime(9, 0), amountMl = 5000))

            val summary = summaryOf(BuiltInHabit.Hydration, drinks = drinks)

            assertEquals(HabitDayStatus.Done, summary.state.status)
        }

    @Test
    fun `several drinks add up towards hydration`() =
        runTest {
            val drinks =
                listOf(
                    Drink(dateTime = HABIT_TODAY.atTime(8, 0), amountMl = 1500),
                    Drink(dateTime = HABIT_TODAY.atTime(13, 0), amountMl = 1500),
                    Drink(dateTime = HABIT_TODAY.atTime(20, 30), amountMl = 1500),
                )

            val summary = summaryOf(BuiltInHabit.Hydration, drinks = drinks)

            assertEquals(HabitDayStatus.Done, summary.state.status)
        }

    @Test
    fun `a drink logged late in the evening still counts for today`() =
        runTest {
            val drinks = listOf(Drink(dateTime = HABIT_TODAY.atTime(23, 59), amountMl = 5000))

            val summary = summaryOf(BuiltInHabit.Hydration, drinks = drinks)

            assertEquals(HabitDayStatus.Done, summary.state.status)
        }

    @Test
    fun `too little water leaves hydration open`() =
        runTest {
            val drinks = listOf(Drink(dateTime = HABIT_TODAY.atTime(9, 0), amountMl = 300))

            val summary = summaryOf(BuiltInHabit.Hydration, drinks = drinks)

            assertEquals(HabitDayStatus.None, summary.state.status)
        }

    @Test
    fun `calories within the range mark the habit as done`() =
        runTest {
            val summary = summaryOf(BuiltInHabit.Calories, meals = listOf(mealWith(calories = 2300)))

            assertEquals(HabitDayStatus.Done, summary.state.status)
        }

    @Test
    fun `too few calories leave the habit open`() =
        runTest {
            val summary = summaryOf(BuiltInHabit.Calories, meals = listOf(mealWith(calories = 900)))

            assertEquals(HabitDayStatus.None, summary.state.status)
        }
}
