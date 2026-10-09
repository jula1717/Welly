package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.fake.FakeActivityRepository
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
}
