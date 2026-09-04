package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.DailyIntakeTotals
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import javax.inject.Inject

class CalculateDailyIntakeTotalsUseCase
    @Inject
    constructor() {
        operator fun invoke(
            meals: List<Meal>,
            drinks: List<Drink>,
        ): DailyIntakeTotals {
            val macros = meals.map { it.macros } + drinks.mapNotNull { it.macros }
            return DailyIntakeTotals(
                calories = macros.sumOf { it.calories },
                protein = macros.sumOf { it.protein },
                carbs = macros.sumOf { it.carbs },
                fat = macros.sumOf { it.fat },
                fiber = macros.sumOf { it.fiber },
                hydrationMl = drinks.sumOf { it.amountMl },
            )
        }
    }
