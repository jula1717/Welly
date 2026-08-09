package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetMealsForDayUseCase
    @Inject
    constructor(
        private val repository: MealRepository,
    ) {
        operator fun invoke(date: LocalDate): Flow<List<Meal>> = repository.getMealsForDay(date)
    }
