package com.jula1717.welly.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jula1717.welly.data.local.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitCompletionDao {
    @Upsert
    suspend fun upsert(completion: HabitCompletionEntity)

    @Query("DELETE FROM habit_completions WHERE habitId = :habitId AND epochDay = :epochDay")
    suspend fun delete(
        habitId: Long,
        epochDay: Long,
    )

    @Query(
        "SELECT * FROM habit_completions " +
            "WHERE habitId = :habitId AND epochDay BETWEEN :fromEpochDay AND :toEpochDay",
    )
    fun getCompletions(
        habitId: Long,
        fromEpochDay: Long,
        toEpochDay: Long,
    ): Flow<List<HabitCompletionEntity>>
}
