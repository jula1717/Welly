package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class GetHabitsUseCaseTest {
    private val profile = UserProfile(
        sex = BiologicalSex.Female,
        dateOfBirth = LocalDate.of(1990, 1, 1),
        heightCm = 170,
        weightKg = 60.0,
        activityLevel = ActivityLevel.Light,
        goal = NutritionGoal.Maintain,
        workoutsPerWeek = 5,
    )

    @Test
    fun `lists built-in habits first and keeps creation order within groups`() =
        runTest {
            val repository = FakeHabitRepository(
                listOf(
                    habit(id = 1),
                    habit(id = 2, builtIn = BuiltInHabit.Macros),
                    habit(id = 3, builtIn = BuiltInHabit.Calories),
                    habit(id = 4),
                ),
            )

            val ids = getHabitsUseCase(repository)().first().map { it.id }

            assertEquals(listOf(2L, 3L, 1L, 4L), ids)
        }

    @Test
    fun `the workouts habit takes its weekly quota from the profile`() =
        runTest {
            val repository = FakeHabitRepository(listOf(habit(id = 1, builtIn = BuiltInHabit.Workouts)))

            val workouts = getHabitsUseCase(repository, profile)().first().single()

            assertEquals(HabitFrequency(5, 1, HabitPeriodUnit.Week), workouts.frequency)
        }

    @Test
    fun `the workouts quota falls back to the default until a profile is saved`() =
        runTest {
            val repository = FakeHabitRepository(listOf(habit(id = 1, builtIn = BuiltInHabit.Workouts)))

            val workouts = getHabitsUseCase(repository)().first().single()

            assertEquals(UserProfile.DEFAULT_WORKOUTS_PER_WEEK, workouts.frequency.times)
        }

    @Test
    fun `other habits keep their own frequency`() =
        runTest {
            val everyThirdDay = HabitFrequency(1, 3, HabitPeriodUnit.Day)
            val repository =
                FakeHabitRepository(
                    listOf(
                        habit(id = 1, builtIn = BuiltInHabit.Calories),
                        habit(id = 2).copy(frequency = everyThirdDay),
                    ),
                )

            val frequencies = getHabitsUseCase(repository, profile)().first().map { it.frequency }

            assertEquals(listOf(HabitFrequency.Daily, everyThirdDay), frequencies)
        }

    @Test
    fun `a single habit also follows the profile quota`() =
        runTest {
            val repository = FakeHabitRepository(listOf(habit(id = 1, builtIn = BuiltInHabit.Workouts)))

            val workouts = getHabitUseCase(repository, profile)(1).first()

            assertEquals(5, workouts?.frequency?.times)
        }

    private fun habit(
        id: Long,
        builtIn: BuiltInHabit? = null,
    ) = Habit(
        id = id,
        builtIn = builtIn,
        name = null,
        points = 10,
        frequency = HabitFrequency.Daily,
        startDate = LocalDate.of(2026, 1, 1),
    )
}
