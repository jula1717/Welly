package com.jula1717.welly.presentation.habits

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitDayStatus
import com.jula1717.welly.domain.usecase.EnsureBuiltInHabitsUseCase
import com.jula1717.welly.domain.usecase.HABIT_TODAY
import com.jula1717.welly.domain.usecase.SetHabitCompletionCountUseCase
import com.jula1717.welly.domain.usecase.getHabitSummariesUseCase
import com.jula1717.welly.domain.usecase.habitClock
import com.jula1717.welly.domain.usecase.testHabit
import com.jula1717.welly.fake.FakeHabitRepository
import com.jula1717.welly.presentation.util.SelectedDateHolder
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HabitsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val clock = habitClock()
    private val custom = testHabit(id = 1)

    private fun TestScope.buildViewModel(
        habits: FakeHabitRepository,
        selectedDateHolder: SelectedDateHolder = SelectedDateHolder(clock),
    ): HabitsViewModel {
        val viewModel = HabitsViewModel(
            getHabitSummaries = getHabitSummariesUseCase(habits, clock),
            ensureBuiltInHabits = EnsureBuiltInHabitsUseCase(habits, clock),
            setHabitCompletionCount = SetHabitCompletionCountUseCase(habits, clock),
            selectedDateHolder = SelectedDateHolder(clock),
            clock = clock,
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        return viewModel
    }

    private fun HabitsViewModel.habits() = uiState.value.habits

    private fun HabitsViewModel.summaryOf(id: Long) = habits().first { it.habit.id == id }

    @Test
    fun `starts on today and creates the built-in habits above the custom ones`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(custom)))

            val state = viewModel.uiState.value

            assertEquals(HABIT_TODAY, state.date)
            assertFalse(state.canGoToNextDay)
            assertEquals(
                BuiltInHabit.entries.toList() + null,
                state.habits.map { it.habit.builtIn },
            )
        }

    @Test
    fun `checking a custom habit marks the selected day as done and unchecking clears it`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(custom)))

            viewModel.onEvent(HabitsUiEvent.OnCountChanged(custom.id, 1))
            val done = viewModel.summaryOf(custom.id)

            viewModel.onEvent(HabitsUiEvent.OnCountChanged(custom.id, 0))
            val cleared = viewModel.summaryOf(custom.id)

            assertEquals(HabitDayStatus.Done, done.state.status)
            assertEquals(HabitDayStatus.None, cleared.state.status)
        }

    @Test
    fun `a check applies to the day that is shown`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(custom)))
            viewModel.onEvent(HabitsUiEvent.OnPreviousDay)

            viewModel.onEvent(HabitsUiEvent.OnCountChanged(custom.id, 1))

            val yesterday = viewModel.summaryOf(custom.id)
            assertEquals(HABIT_TODAY.minusDays(1), viewModel.uiState.value.date)
            assertEquals(HabitDayStatus.Done, yesterday.state.status)

            viewModel.onEvent(HabitsUiEvent.OnNextDay)
            val today = viewModel.summaryOf(custom.id)
            assertEquals(HabitDayStatus.None, today.state.status)
        }

    @Test
    fun `cannot go past today`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(custom)))

            viewModel.onEvent(HabitsUiEvent.OnNextDay)
            viewModel.onEvent(HabitsUiEvent.OnDateSelected(HABIT_TODAY.plusDays(5)))

            assertEquals(HABIT_TODAY, viewModel.uiState.value.date)
        }

    @Test
    fun `going back enables stepping forward again`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(custom)))

            viewModel.onEvent(HabitsUiEvent.OnDateSelected(HABIT_TODAY.minusDays(3)))

            assertTrue(viewModel.uiState.value.canGoToNextDay)
        }

    @Test
    fun `switched-off built-in habits are not listed`() =
        runTest {
            val off = testHabit(id = 1, builtIn = BuiltInHabit.Workouts, isEnabled = false)
            val viewModel = buildViewModel(FakeHabitRepository(listOf(off)))

            val builtIns = viewModel.habits().mapNotNull { it.habit.builtIn }

            assertFalse(BuiltInHabit.Workouts in builtIns)
            assertEquals(BuiltInHabit.entries.size - 1, builtIns.size)
        }
}
