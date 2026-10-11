package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class UpdateHabitUseCaseTest {
    private val threeTimesAWeek = HabitFrequency(times = 3, periodLength = 1, periodUnit = HabitPeriodUnit.Week)

    @Test
    fun `trims the name of a custom habit and updates points`() =
        runTest {
            val custom = testHabit(id = 1)
            val repository = FakeHabitRepository(listOf(custom))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(custom.copy(name = "Reading  ", points = 20))

            val updated = repository.getHabits().first().single()
            assertEquals("Reading", updated.name)
            assertEquals(20, updated.points)
        }

    @Test
    fun `custom habit frequency can be changed`() =
        runTest {
            val custom = testHabit(id = 1)
            val repository = FakeHabitRepository(listOf(custom))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(custom.copy(frequency = threeTimesAWeek))

            val updated = repository.getHabits().first().single()
            assertEquals(threeTimesAWeek, updated.frequency)
        }

    @Test
    fun `blank name resets a built-in habit to its default`() =
        runTest {
            val builtIn = testHabit(id = 1, builtIn = BuiltInHabit.Macros).copy(name = "Custom")
            val repository = FakeHabitRepository(listOf(builtIn))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(builtIn.copy(name = "  "))

            val updated = repository.getHabits().first().single()
            assertEquals(null, updated.name)
        }

    @Test
    fun `built-in habit keeps its frequency and identity`() =
        runTest {
            val builtIn = testHabit(id = 1, builtIn = BuiltInHabit.Workouts, frequency = HabitFrequency.Weekly)
            val repository = FakeHabitRepository(listOf(builtIn))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(
                builtIn.copy(
                    builtIn = null,
                    frequency = threeTimesAWeek,
                    startDate = LocalDate.of(2020, 1, 1),
                    points = 30,
                ),
            )

            val updated = repository.getHabits().first().single()
            assertEquals(BuiltInHabit.Workouts, updated.builtIn)
            assertEquals(HabitFrequency.Weekly, updated.frequency)
            assertEquals(builtIn.startDate, updated.startDate)
            assertEquals(30, updated.points)
        }

    @Test
    fun `can disable a built-in habit`() =
        runTest {
            val builtIn = testHabit(id = 1, builtIn = BuiltInHabit.Workouts)
            val repository = FakeHabitRepository(listOf(builtIn))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(builtIn.copy(isEnabled = false))

            val updated = repository.getHabits().first().single()
            assertEquals(false, updated.isEnabled)
        }

    @Test
    fun `ignores unknown habit`() =
        runTest {
            val repository = FakeHabitRepository(listOf(testHabit(id = 1)))

            UpdateHabitUseCase(repository, ValidateHabitUseCase())(testHabit(id = 99).copy(name = "Ghost"))

            val habits = repository.getHabits().first()
            assertTrue(habits.none { it.name == "Ghost" })
            assertEquals(1, habits.size)
        }

    @Test
    fun `rejects invalid input without saving it`() =
        runTest {
            val custom = testHabit(id = 1)
            val repository = FakeHabitRepository(listOf(custom))
            val update = UpdateHabitUseCase(repository, ValidateHabitUseCase())
            val noPeriod = HabitFrequency(times = 1, periodLength = 0, periodUnit = HabitPeriodUnit.Day)

            val results =
                listOf(
                    runCatching { update(custom.copy(name = " ")) },
                    runCatching { update(custom.copy(points = -5)) },
                    runCatching { update(custom.copy(frequency = noPeriod)) },
                )

            assertTrue(results.all { it.exceptionOrNull() is IllegalArgumentException })
            assertEquals(custom, repository.getHabits().first().single())
        }
}
