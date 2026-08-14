package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.Drink
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface DrinkRepository {
    suspend fun addDrink(drink: Drink)

    fun getDrinksForDay(date: LocalDate): Flow<List<Drink>>
}
