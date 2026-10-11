package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitDayState
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetHabitSummariesUseCaseTest {
    private val getPeriod = GetHabitPeriodUseCase()
    private val twicePerWeek = HabitFrequency(times = 2, periodLength = 1, periodUnit = HabitPeriodUnit.Week)

    private fun useCase(habits: FakeHabitRepository) =
        GetHabitSummariesUseCase(
            getHabits = GetHabitsUseCase(habits),
            getHabitLogs = getHabitLogsUseCase(habits),
            getPeriod = getPeriod,
            calculateDayStates = CalculateHabitDayStatesUseCase(getPeriod),
            clock = habitClock(),
        )

    @Test
    fun `lists enabled habits with built-in first`() =
        runTest {
            val habits =
                FakeHabitRepository(
                    listOf(
                        testHabit(id = 1),
                        testHabit(id = 2, builtIn = BuiltInHabit.Calories),
                        testHabit(id = 3, builtIn = BuiltInHabit.Workouts, isEnabled = false),
                    ),
                )

            val summaries = useCase(habits)(HABIT_TODAY).first()

            assertEquals(listOf(2L, 1L), summaries.map { it.habit.id })
        }

    @Test
    fun `reports progress within the period and a covered day`() =
        runTest {
            val habit = testHabit(id = 1, frequency = twicePerWeek)
            val habits = FakeHabitRepository(listOf(habit))
            habits.setCompletionCount(1, HABIT_MONDAY, 1)
            habits.setCompletionCount(1, HABIT_MONDAY.plusDays(1), 1)

            val summary = useCase(habits)(HABIT_TODAY).first().single()

            assertEquals(2, summary.periodCount)
            assertEquals(HabitDayStatus.Covered, summary.state.status)
        }

    @Test
    fun `reports the checked-in day as done`() =
        runTest {
            val habits = FakeHabitRepository(listOf(testHabit(id = 1)))
            habits.setCompletionCount(1, HABIT_TODAY, 1)

            val summary = useCase(habits)(HABIT_TODAY).first().single()

            assertEquals(HabitDayStatus.Done, summary.state.status)
            assertEquals(1, summary.state.count)
        }

    @Test
    fun `a future day is empty`() =
        runTest {
            val habits = FakeHabitRepository(listOf(testHabit(id = 1)))

            val summary = useCase(habits)(HABIT_TODAY.plusDays(1)).first().single()

            assertEquals(HabitDayState.Empty, summary.state)
        }

    @Test
    fun `no habits gives an empty list`() =
        runTest {
            assertEquals(emptyList<Any>(), useCase(FakeHabitRepository())(HABIT_TODAY).first())
        }
}
