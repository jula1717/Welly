package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.repository.DrinkRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class GetDrinksForDayUseCase
    @Inject
    constructor(
        private val repository: DrinkRepository,
    ) {
        operator fun invoke(date: LocalDate): Flow<List<Drink>> = repository.getDrinksForDay(date)
    }
