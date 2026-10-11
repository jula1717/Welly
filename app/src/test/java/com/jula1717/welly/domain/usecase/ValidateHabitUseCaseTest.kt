package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateHabitUseCaseTest {
    private val useCase = ValidateHabitUseCase()

    @Test
    fun `accepts a custom habit with a name`() {
        assertTrue(useCase(testHabit().copy(name = "Stretching")))
    }

    @Test
    fun `rejects a custom habit with a blank or leading-space name`() {
        assertFalse(useCase(testHabit().copy(name = null)))
        assertFalse(useCase(testHabit().copy(name = "  ")))
        assertFalse(useCase(testHabit().copy(name = " Stretching")))
    }

    @Test
    fun `built-in habit may keep its default name`() {
        val builtIn = testHabit(builtIn = BuiltInHabit.Hydration)

        assertTrue(useCase(builtIn.copy(name = null)))
        assertTrue(useCase(builtIn.copy(name = "")))
        assertTrue(useCase(builtIn.copy(name = "Water")))
        assertFalse(useCase(builtIn.copy(name = " Water")))
    }

    @Test
    fun `rejects negative points but allows zero`() {
        assertFalse(useCase(testHabit().copy(points = -1)))
        assertTrue(useCase(testHabit().copy(points = 0)))
    }

    @Test
    fun `rejects frequencies that are not positive`() {
        assertFalse(useCase(testHabit(frequency = HabitFrequency(0, 1, HabitPeriodUnit.Day))))
        assertFalse(useCase(testHabit(frequency = HabitFrequency(1, 0, HabitPeriodUnit.Day))))
        assertTrue(useCase(testHabit(frequency = HabitFrequency(3, 10, HabitPeriodUnit.Day))))
    }
}
