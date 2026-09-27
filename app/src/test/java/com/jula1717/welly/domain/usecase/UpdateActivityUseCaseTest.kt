package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class UpdateActivityUseCaseTest {
    private val repository = FakeActivityRepository()
    private val updateActivity = UpdateActivityUseCase(repository)

    @Test
    fun `updates activity with trailing whitespace trimmed`() =
        runTest {
            updateActivity(Activity(id = 1, name = "Yoga ", type = ActivityType.Strength))

            assertEquals(listOf(Activity(id = 1, name = "Yoga", type = ActivityType.Strength)), repository.updated)
        }

    private class FakeActivityRepository : ActivityRepository {
        val updated = mutableListOf<Activity>()

        override suspend fun addActivity(activity: Activity) = Unit

        override suspend fun updateActivity(activity: Activity) {
            updated += activity
        }

        override suspend fun archiveActivity(id: Long) = Unit

        override fun getActivities(): Flow<List<Activity>> = emptyFlow()
    }
}
