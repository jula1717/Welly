package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EnsureBuiltInHabitsUseCaseTest {
    private fun useCase(repository: FakeHabitRepository) = EnsureBuiltInHabitsUseCase(repository, habitClock())

    @Test
    fun `adds every built-in habit with defaults`() =
        runTest {
            val repository = FakeHabitRepository()

            useCase(repository)()

            val habits = repository.getHabits().first()
            assertEquals(BuiltInHabit.entries.toSet(), habits.mapNotNull { it.builtIn }.toSet())
            assertEquals(BuiltInHabit.entries.size, habits.size)
            habits.forEach { habit ->
                val isWorkouts = habit.builtIn == BuiltInHabit.Workouts
                assertEquals(if (isWorkouts) HabitFrequency.Weekly else HabitFrequency.Daily, habit.frequency)
                assertEquals(HABIT_TODAY, habit.startDate)
                assertEquals(null, habit.name)
                assertEquals(true, habit.isEnabled)
            }
        }

    @Test
    fun `does not add habits twice`() =
        runTest {
            val repository = FakeHabitRepository()
            val useCase = useCase(repository)

            useCase()
            useCase()

            assertEquals(BuiltInHabit.entries.size, repository.getHabits().first().size)
        }

    @Test
    fun `adds only the missing built-in habits and keeps edits to existing ones`() =
        runTest {
            val edited = testHabit(id = 1, builtIn = BuiltInHabit.Calories).copy(name = "My kcal", points = 50)
            val repository = FakeHabitRepository(listOf(edited))

            useCase(repository)()

            val habits = repository.getHabits().first()
            assertEquals(BuiltInHabit.entries.size, habits.size)
            assertEquals(edited, habits.first { it.builtIn == BuiltInHabit.Calories })
        }
}
