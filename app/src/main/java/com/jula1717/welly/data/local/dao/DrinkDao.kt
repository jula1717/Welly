package com.jula1717.welly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jula1717.welly.data.local.entity.DrinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Insert
    suspend fun insertDrink(drink: DrinkEntity)

    @Query("SELECT * FROM drinks WHERE dateTimeEpochSecond BETWEEN :startEpoch AND :endEpoch")
    fun getDrinksInRange(
        startEpoch: Long,
        endEpoch: Long,
    ): Flow<List<DrinkEntity>>
}
