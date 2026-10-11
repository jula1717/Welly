package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.repository.DrinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeDrinkRepository(
    private val drinks: List<Drink> = emptyList(),
) : DrinkRepository {
    override suspend fun addDrink(drink: Drink) = Unit

    override fun getDrinksInRange(range: ClosedRange<LocalDate>): Flow<List<Drink>> =
        flowOf(drinks.filter { it.dateTime.toLocalDate() in range })
}
