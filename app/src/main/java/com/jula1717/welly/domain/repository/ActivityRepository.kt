package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    suspend fun addActivity(activity: Activity)

    suspend fun updateActivity(activity: Activity)

    suspend fun archiveActivity(id: Long)

    fun getActivities(): Flow<List<Activity>>
}
