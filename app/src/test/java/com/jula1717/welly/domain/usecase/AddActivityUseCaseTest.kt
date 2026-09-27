package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AddActivityUseCaseTest {
    private val repository = FakeActivityRepository()
    private val addActivity = AddActivityUseCase(repository)

    @Test
    fun `adds activity with trailing whitespace trimmed`() =
        runTest {
            addActivity(Activity(name = "Jogging  ", type = ActivityType.Cardio))

            assertEquals(listOf(Activity(name = "Jogging", type = ActivityType.Cardio)), repository.added)
        }

    private class FakeActivityRepository : ActivityRepository {
        val added = mutableListOf<Activity>()

        override suspend fun addActivity(activity: Activity) {
            added += activity
        }

        override suspend fun updateActivity(activity: Activity) = Unit

        override suspend fun archiveActivity(id: Long) = Unit

        override fun getActivities(): Flow<List<Activity>> = emptyFlow()
    }
}
