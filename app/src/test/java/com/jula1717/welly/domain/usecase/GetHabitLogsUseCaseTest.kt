package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetHabitLogsUseCaseTest {
    @Test
    fun `custom habit logs come from stored check-ins in range`() =
        runTest {
            val habit = testHabit(id = 1)
            val otherHabit = testHabit(id = 2)
            val habits = FakeHabitRepository(listOf(habit, otherHabit))
            habits.setCompletionCount(habit.id, HABIT_TODAY, 2)
            habits.setCompletionCount(habit.id, HABIT_MONDAY.minusDays(1), 1)
            habits.setCompletionCount(otherHabit.id, HABIT_TODAY, 5)

            val logs = getHabitLogsUseCase(habits)(habit, HABIT_MONDAY..HABIT_TODAY).first()

            assertEquals(mapOf(HABIT_TODAY to HabitDayLog(count = 2)), logs)
        }

    @Test
    fun `built-in habit logs are derived from logged data in range`() =
        runTest {
            val run = Activity(id = 1, name = "Run", type = ActivityType.Cardio)
            val workouts =
                listOf(
                    Workout(id = 1, activity = run, dateTime = HABIT_TODAY.atTime(7, 0)),
                    Workout(id = 2, activity = run, dateTime = HABIT_MONDAY.minusDays(1).atTime(7, 0)),
                )
            val habit = testHabit(builtIn = BuiltInHabit.Workouts)

            val logs = getHabitLogsUseCase(workouts = workouts)(habit, HABIT_MONDAY..HABIT_TODAY).first()

            val detail = HabitDayDetail.Workouts(setOf(ActivityType.Cardio))
            assertEquals(mapOf(HABIT_TODAY to HabitDayLog(count = 1, detail = detail)), logs)
        }
}
