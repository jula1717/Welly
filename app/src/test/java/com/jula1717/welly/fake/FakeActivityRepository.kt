package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeActivityRepository(
    initial: List<Activity> = emptyList(),
) : ActivityRepository {
    private val activities = MutableStateFlow(initial)

    val added = mutableListOf<Activity>()
    val updated = mutableListOf<Activity>()
    val archivedIds = mutableListOf<Long>()

    override suspend fun addActivity(activity: Activity) {
        added += activity
        activities.update { list -> list + activity.copy(id = (list.maxOfOrNull { it.id } ?: 0L) + 1) }
    }

    override suspend fun updateActivity(activity: Activity) {
        updated += activity
        activities.update { list -> list.map { if (it.id == activity.id) activity else it } }
    }

    override suspend fun archiveActivity(id: Long) {
        archivedIds += id
        activities.update { list -> list.filterNot { it.id == id } }
    }

    override fun getActivities(): Flow<List<Activity>> = activities
}
