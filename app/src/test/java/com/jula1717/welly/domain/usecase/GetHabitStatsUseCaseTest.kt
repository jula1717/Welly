package com.jula1717.welly.domain.usecase

import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetHabitStatsUseCaseTest {
    private val getPeriod = GetHabitPeriodUseCase()

    private fun useCase(habits: FakeHabitRepository) =
        GetHabitStatsUseCase(
            habitRepository = habits,
            getHabitLogs = getHabitLogsUseCase(habits),
            calculateStats = CalculateHabitStatsUseCase(getPeriod, CalculateHabitDayStatesUseCase(getPeriod)),
            clock = habitClock(),
        )

    @Test
    fun `emits stats built from the stored check-ins`() =
        runTest {
            val habits = FakeHabitRepository(listOf(testHabit(id = 1)))
            (11..14).forEach { habits.setCompletionCount(1, HABIT_TODAY.withDayOfMonth(it), 1) }

            val stats = useCase(habits)(1).first()

            assertEquals(4, stats?.currentStreak)
            assertEquals(12, stats?.monthlyCounts?.size)
        }

    @Test
    fun `emits null for an unknown habit`() =
        runTest {
            assertNull(useCase(FakeHabitRepository())(99).first())
        }
}
