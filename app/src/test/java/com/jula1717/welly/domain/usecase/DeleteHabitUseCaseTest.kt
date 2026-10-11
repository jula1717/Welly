package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeleteHabitUseCaseTest {
    private val builtIn = testHabit(id = 1, builtIn = BuiltInHabit.Calories)
    private val custom = testHabit(id = 2)
    private val repository = FakeHabitRepository(listOf(builtIn, custom))
    private val useCase = DeleteHabitUseCase(repository)

    @Test
    fun `deletes custom habit`() =
        runTest {
            useCase(custom.id)

            assertEquals(listOf(2L), repository.deletedIds)
        }

    @Test
    fun `refuses to delete built-in habit`() =
        runTest {
            val result = runCatching { useCase(builtIn.id) }

            assertTrue(result.exceptionOrNull() is IllegalArgumentException)
            assertTrue(repository.deletedIds.isEmpty())
        }

    @Test
    fun `ignores unknown id`() =
        runTest {
            useCase(99)

            assertTrue(repository.deletedIds.isEmpty())
        }
}
