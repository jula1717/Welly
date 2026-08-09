package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.MealDao
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.repository.MealRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import javax.inject.Inject

class MealRepositoryImpl
    @Inject
    constructor(
        private val mealDao: MealDao,
    ) : MealRepository {
        override suspend fun addMeal(meal: Meal) {
            mealDao.insertMeal(meal.toEntity())
        }

        override fun getMealsForDay(date: LocalDate): Flow<List<Meal>> {
            val startOfDay = date.atStartOfDay().toEpochSecond(ZoneOffset.UTC)
            val endOfDay = date.atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC)
            return mealDao.getMealsForDay(startOfDay, endOfDay).map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }
