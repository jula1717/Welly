package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeMealRepository(
    private val meals: List<Meal> = emptyList(),
) : MealRepository {
    override suspend fun addMeal(meal: Meal) = Unit

    override fun getMealsInRange(range: ClosedRange<LocalDate>): Flow<List<Meal>> =
        flowOf(meals.filter { it.dateTime.toLocalDate() in range })
}
