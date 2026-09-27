package com.jula1717.welly.data.repository

import com.jula1717.welly.data.local.dao.ActivityDao
import com.jula1717.welly.data.local.mapper.toDomain
import com.jula1717.welly.data.local.mapper.toEntity
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityRepositoryImpl
    @Inject
    constructor(
        private val activityDao: ActivityDao,
    ) : ActivityRepository {
        override suspend fun addActivity(activity: Activity) {
            activityDao.insertActivity(activity.toEntity())
        }

        override suspend fun updateActivity(activity: Activity) {
            activityDao.updateActivity(activity.toEntity())
        }

        override suspend fun archiveActivity(id: Long) {
            activityDao.archiveActivity(id)
        }

        override fun getActivities(): Flow<List<Activity>> =
            activityDao.getActivities().map { entities ->
                entities.map { it.toDomain() }
            }
    }
