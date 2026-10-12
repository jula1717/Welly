package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetHabitEnabledUseCaseTest {
    private val builtIn = testHabit(id = 1, builtIn = BuiltInHabit.Workouts)
    private val repository = FakeHabitRepository(listOf(builtIn))
    private val useCase = SetHabitEnabledUseCase(repository)

    @Test
    fun `turns a habit off and on again`() =
        runTest {
            useCase(builtIn.id, false)
            val off = repository.getHabits().first().single()

            useCase(builtIn.id, true)
            val on = repository.getHabits().first().single()

            assertEquals(false, off.isEnabled)
            assertEquals(builtIn, on)
        }

    @Test
    fun `ignores unknown habit`() =
        runTest {
            useCase(99, false)

            assertEquals(listOf(builtIn), repository.getHabits().first())
        }
}
