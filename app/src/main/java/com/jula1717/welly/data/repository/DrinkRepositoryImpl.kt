package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.data.local.util.toEpochSecondRange
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.repository.DrinkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class DrinkRepositoryImpl
    @Inject
    constructor(
        private val drinkDao: DrinkDao,
    ) : DrinkRepository {
        override suspend fun addDrink(drink: Drink) {
            drinkDao.insertDrink(drink.toEntity())
        }

        override fun getDrinksInRange(range: ClosedRange<LocalDate>): Flow<List<Drink>> {
            val (start, end) = range.toEpochSecondRange()
            return drinkDao.getDrinksInRange(start, end).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }
