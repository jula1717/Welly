package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitCompletion
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetHabitCompletionCountUseCaseTest {
    private val custom = testHabit(id = 1)
    private val builtIn = testHabit(id = 2, builtIn = BuiltInHabit.Calories)
    private val repository = FakeHabitRepository(listOf(custom, builtIn))
    private val useCase = SetHabitCompletionCountUseCase(repository, habitClock())

    private suspend fun completions(habitId: Long) =
        repository.getCompletions(habitId, HABIT_MONDAY, HABIT_TODAY).first()

    @Test
    fun `stores the count for a day`() =
        runTest {
            useCase(custom.id, HABIT_TODAY, 2)

            assertEquals(listOf(HabitCompletion(custom.id, HABIT_TODAY, 2)), completions(custom.id))
        }

    @Test
    fun `zero or negative count clears the day`() =
        runTest {
            useCase(custom.id, HABIT_TODAY, 2)

            useCase(custom.id, HABIT_TODAY, -3)

            assertTrue(completions(custom.id).isEmpty())
        }

    @Test
    fun `refuses to check in a built-in habit`() =
        runTest {
            val result = runCatching { useCase(builtIn.id, HABIT_TODAY, 1) }

            assertTrue(result.exceptionOrNull() is IllegalArgumentException)
            assertTrue(completions(builtIn.id).isEmpty())
        }

    @Test
    fun `refuses to check in a future day`() =
        runTest {
            val result = runCatching { useCase(custom.id, HABIT_TODAY.plusDays(1), 1) }

            assertTrue(result.exceptionOrNull() is IllegalArgumentException)
            assertTrue(completions(custom.id).isEmpty())
        }

    @Test
    fun `ignores unknown habit`() =
        runTest {
            useCase(99, HABIT_TODAY, 1)

            assertTrue(completions(99).isEmpty())
        }
}
