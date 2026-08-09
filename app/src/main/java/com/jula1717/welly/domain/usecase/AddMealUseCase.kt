package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.repository.MealRepository
import javax.inject.Inject

class AddMealUseCase
    @Inject
    constructor(
        private val repository: MealRepository,
    ) {
        suspend operator fun invoke(meal: Meal) = repository.addMeal(meal)
    }
