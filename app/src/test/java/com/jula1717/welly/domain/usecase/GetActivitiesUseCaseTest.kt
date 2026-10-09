package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.fake.FakeActivityRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Locale

class GetActivitiesUseCaseTest {
    private val originalLocale = Locale.getDefault()

    private val repository = FakeActivityRepository(
        listOf(
            activity(id = 1, name = "Zumba"),
            activity(id = 2, name = "golf"),
            activity(id = 3, name = "Jogging"),
            activity(id = 4, name = "Żeglarstwo"),
        ),
    )

    @After
    fun tearDown() {
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `sorts by name using polish alphabet rules`() =
        runTest {
            Locale.setDefault(Locale.forLanguageTag("pl-PL"))

            val names = GetActivitiesUseCase(repository)().first().map { it.name }

            assertEquals(listOf("golf", "Jogging", "Zumba", "Żeglarstwo"), names)
        }

    @Test
    fun `sorts by name using english alphabet rules`() =
        runTest {
            Locale.setDefault(Locale.forLanguageTag("en-US"))

            val names = GetActivitiesUseCase(repository)().first().map { it.name }

            assertEquals(listOf("golf", "Jogging", "Żeglarstwo", "Zumba"), names)
        }

    private fun activity(
        id: Long,
        name: String,
    ) = Activity(id = id, name = name, type = ActivityType.Cardio)
}
