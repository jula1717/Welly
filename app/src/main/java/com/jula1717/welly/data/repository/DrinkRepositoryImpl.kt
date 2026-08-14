package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.DrinkDao
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.data.local.util.toDayEpochSecondRange
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

        override fun getDrinksForDay(date: LocalDate): Flow<List<Drink>> {
            val (startOfDay, endOfDay) = date.toDayEpochSecondRange()
            return drinkDao.getDrinksForDay(startOfDay, endOfDay).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }
