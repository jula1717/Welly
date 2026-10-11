package com.jula1717.welly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jula1717.welly.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert
    suspend fun insertMeal(meal: MealEntity)

    @Query("SELECT * FROM meals WHERE dateTimeEpochSecond BETWEEN :startEpoch AND :endEpoch")
    fun getMealsInRange(
        startEpoch: Long,
        endEpoch: Long,
    ): Flow<List<MealEntity>>
}
