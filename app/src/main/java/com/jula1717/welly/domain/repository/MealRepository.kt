package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.Meal
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface MealRepository {
    suspend fun addMeal(meal: Meal)

    fun getMealsForDay(date: LocalDate): Flow<List<Meal>>
}
