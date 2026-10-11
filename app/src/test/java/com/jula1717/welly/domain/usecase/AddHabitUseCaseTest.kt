package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AddHabitUseCaseTest {
    private val repository = FakeHabitRepository()
    private val useCase = AddHabitUseCase(repository, ValidateHabitUseCase(), habitClock())

    @Test
    fun `adds a custom habit starting today with a trimmed name`() =
        runTest {
            useCase("Stretching  ", points = 5, frequency = HabitFrequency.Weekly)

            val habit = repository.getHabits().first().single()
            assertEquals("Stretching", habit.name)
            assertEquals(5, habit.points)
            assertEquals(HabitFrequency.Weekly, habit.frequency)
            assertEquals(HABIT_TODAY, habit.startDate)
            assertEquals(null, habit.builtIn)
        }

    @Test
    fun `rejects invalid input without saving`() =
        runTest {
            val noPeriod = HabitFrequency(times = 1, periodLength = 0, periodUnit = HabitPeriodUnit.Day)

            val results =
                listOf(
                    runCatching { useCase(" ", points = 5, frequency = HabitFrequency.Daily) },
                    runCatching { useCase("Reading", points = -1, frequency = HabitFrequency.Daily) },
                    runCatching { useCase("Reading", points = 5, frequency = noPeriod) },
                )

            assertTrue(results.all { it.exceptionOrNull() is IllegalArgumentException })
            assertTrue(repository.getHabits().first().isEmpty())
        }
}
