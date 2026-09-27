package com.jula1717.welly.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jula1717.welly.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Insert
    suspend fun insertActivity(activity: ActivityEntity)

    @Update
    suspend fun updateActivity(activity: ActivityEntity)

    @Query("UPDATE activities SET isArchived = 1 WHERE id = :id")
    suspend fun archiveActivity(id: Long)

    @Query("SELECT * FROM activities WHERE isArchived = 0")
    fun getActivities(): Flow<List<ActivityEntity>>
}
