package com.jula1717.welly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.jula1717.welly.data.local.entity.WorkoutEntity
import com.jula1717.welly.data.local.relation.WorkoutWithActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    @Insert
    suspend fun insert(workout: WorkoutEntity)

    @Transaction
    @Query("SELECT * FROM workouts WHERE dateTimeEpochSecond BETWEEN :startOfDayEpoch AND :endOfDayEpoch")
    fun getWorkoutsForDay(
        startOfDayEpoch: Long,
        endOfDayEpoch: Long,
    ): Flow<List<WorkoutWithActivity>>
}
