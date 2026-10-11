package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class GetHabitsUseCaseTest {
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

            val ids = GetHabitsUseCase(repository)().first().map { it.id }

            assertEquals(listOf(2L, 3L, 1L, 4L), ids)
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
