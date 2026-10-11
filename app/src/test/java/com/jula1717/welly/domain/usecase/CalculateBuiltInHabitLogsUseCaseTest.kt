package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.domain.model.Workout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class CalculateBuiltInHabitLogsUseCaseTest {
    private val useCase = CalculateBuiltInHabitLogsUseCase(CalculateDailyIntakeTotalsUseCase())
    private val targets = testTargets()
    private val day = HABIT_TODAY

    private fun meal(
        calories: Int = 0,
        protein: Int = 0,
        carbs: Int = 0,
        fat: Int = 0,
        date: LocalDate = day,
    ) = Meal(
        dateTime = date.atTime(12, 0),
        type = MealType.Lunch,
        description = "",
        macros = MealMacros(calories = calories, protein = protein, carbs = carbs, fat = fat, fiber = 0),
    )

    private fun logs(
        builtIn: BuiltInHabit,
        meals: List<Meal> = emptyList(),
        drinks: List<Drink> = emptyList(),
        workouts: List<Workout> = emptyList(),
    ) = if (builtIn == BuiltInHabit.Workouts) {
        useCase.forWorkouts(workouts)
    } else {
        useCase.forNutrition(builtIn, meals, drinks, targets)
    }

    @Test
    fun `calories habit is met when intake is within the range`() {
        val result = logs(BuiltInHabit.Calories, meals = listOf(meal(calories = 1000), meal(calories = 1000)))

        assertEquals(mapOf(day to HabitDayLog(count = 1)), result)
    }

    @Test
    fun `calories habit is not met below or above the range`() {
        val below = logs(BuiltInHabit.Calories, meals = listOf(meal(calories = 1799)))
        val above = logs(BuiltInHabit.Calories, meals = listOf(meal(calories = 2201)))

        assertTrue(below.isEmpty())
        assertTrue(above.isEmpty())
    }

    @Test
    fun `calories from drinks count towards the total`() {
        val drink = Drink(dateTime = day.atTime(9, 0), amountMl = 300, macros = MealMacros(300, 0, 0, 0, 0))

        val result = logs(BuiltInHabit.Calories, meals = listOf(meal(calories = 1600)), drinks = listOf(drink))

        assertEquals(setOf(day), result.keys)
    }

    @Test
    fun `macros habit needs protein carbs and fat within range`() {
        val allWithin = meal(protein = 120, carbs = 250, fat = 60)
        val lowFat = meal(protein = 120, carbs = 250, fat = 10, date = day.minusDays(1))

        val result = logs(BuiltInHabit.Macros, meals = listOf(allWithin, lowFat))

        assertEquals(setOf(day), result.keys)
    }

    @Test
    fun `hydration habit is met at the lower bound`() {
        val enough = Drink(dateTime = day.atTime(10, 0), amountMl = 2000)
        val notEnough = Drink(dateTime = day.minusDays(1).atTime(10, 0), amountMl = 1999)

        val result = logs(BuiltInHabit.Hydration, drinks = listOf(enough, notEnough))

        assertEquals(setOf(day), result.keys)
    }

    @Test
    fun `workouts count every session and collect activity types`() {
        val cardio = Activity(id = 1, name = "Run", type = ActivityType.Cardio)
        val strength = Activity(id = 2, name = "Squats", type = ActivityType.Strength)
        val workouts =
            listOf(
                Workout(id = 1, activity = cardio, dateTime = day.atTime(7, 0)),
                Workout(id = 2, activity = cardio, dateTime = day.atTime(12, 0)),
                Workout(id = 3, activity = strength, dateTime = day.atTime(18, 0)),
                Workout(id = 4, activity = strength, dateTime = day.minusDays(1).atTime(18, 0)),
            )

        val result = logs(BuiltInHabit.Workouts, workouts = workouts)

        val bothTypes = HabitDayDetail.Workouts(setOf(ActivityType.Cardio, ActivityType.Strength))
        assertEquals(HabitDayLog(count = 3, detail = bothTypes), result[day])
        val strengthOnly = HabitDayDetail.Workouts(setOf(ActivityType.Strength))
        assertEquals(HabitDayLog(count = 1, detail = strengthOnly), result[day.minusDays(1)])
    }

    @Test
    fun `workouts are not a nutrition habit`() {
        val result = runCatching { useCase.forNutrition(BuiltInHabit.Workouts, emptyList(), emptyList(), targets) }

        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}
