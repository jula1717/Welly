package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.data.local.util.toEpochSecondRange
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class MealRepositoryImpl
    @Inject
    constructor(
        private val mealDao: MealDao,
    ) : MealRepository {
        override suspend fun addMeal(meal: Meal) {
            mealDao.insertMeal(meal.toEntity())
        }

        override fun getMealsInRange(range: ClosedRange<LocalDate>): Flow<List<Meal>> {
            val (start, end) = range.toEpochSecondRange()
            return mealDao.getMealsInRange(start, end).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }
