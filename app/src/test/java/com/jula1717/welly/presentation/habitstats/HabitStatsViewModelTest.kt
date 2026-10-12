package com.jula1717.welly.presentation.habitstats

import androidx.lifecycle.SavedStateHandle
import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.usecase.HABIT_TODAY
import com.jula1717.welly.domain.usecase.getHabitStatsUseCase
import com.jula1717.welly.domain.usecase.getHabitUseCase
import com.jula1717.welly.domain.usecase.habitClock
import com.jula1717.welly.domain.usecase.testHabit
import com.jula1717.welly.fake.FakeHabitRepository
import com.jula1717.welly.presentation.navigation.HabitStatsDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.YearMonth

class HabitStatsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val clock = habitClock()
    private val habit = testHabit(id = 1)

    private fun TestScope.buildViewModel(
        habits: FakeHabitRepository,
        habitId: Long = habit.id,
    ): HabitStatsViewModel {
        val viewModel = HabitStatsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(HabitStatsDestination.HABIT_ID_KEY to habitId)),
            getHabit = getHabitUseCase(habits),
            getHabitStats = getHabitStatsUseCase(habits, clock),
            clock = clock,
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        return viewModel
    }

    @Test
    fun `shows the habit with its streak and starts on the current month`() =
        runTest {
            val habits = FakeHabitRepository(listOf(habit))
            (13..14).forEach { habits.setCompletionCount(habit.id, HABIT_TODAY.withDayOfMonth(it), 1) }

            val state = buildViewModel(habits).uiState.value

            assertEquals(habit, state.habit)
            assertEquals(2, state.stats?.currentStreak)
            assertEquals(YearMonth.from(HABIT_TODAY), state.visibleMonth)
            assertFalse(state.canGoToNextMonth)
        }

    @Test
    fun `can browse back to the month the habit was first done and no further`() =
        runTest {
            val habits = FakeHabitRepository(listOf(habit))
            habits.setCompletionCount(habit.id, HABIT_TODAY.minusMonths(2), 1)
            habits.setCompletionCount(habit.id, HABIT_TODAY, 1)
            val viewModel = buildViewModel(habits)

            repeat(5) { viewModel.onEvent(HabitStatsUiEvent.OnPreviousMonth) }
            val earliest = viewModel.uiState.value

            assertEquals(YearMonth.from(HABIT_TODAY).minusMonths(2), earliest.visibleMonth)
            assertFalse(earliest.canGoToPreviousMonth)
            assertTrue(earliest.canGoToNextMonth)
        }

    @Test
    fun `stays on the current month while the habit has never been done`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(habit)))

            viewModel.onEvent(HabitStatsUiEvent.OnPreviousMonth)

            assertEquals(YearMonth.from(HABIT_TODAY), viewModel.uiState.value.visibleMonth)
            assertFalse(viewModel.uiState.value.canGoToPreviousMonth)
        }

    @Test
    fun `cannot browse past the current month`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(listOf(habit)))

            viewModel.onEvent(HabitStatsUiEvent.OnNextMonth)

            assertEquals(YearMonth.from(HABIT_TODAY), viewModel.uiState.value.visibleMonth)
        }

    @Test
    fun `asks to leave when the habit does not exist`() =
        runTest {
            val viewModel = buildViewModel(FakeHabitRepository(), habitId = 99)

            assertEquals(HabitStatsEffect.NavigateBack, viewModel.effects.first())
            assertNull(viewModel.uiState.value.habit)
        }

    @Test
    fun `asks to leave once the habit is deleted`() =
        runTest {
            val habits = FakeHabitRepository(listOf(habit))
            val viewModel = buildViewModel(habits)

            habits.deleteHabit(habit.id)

            assertEquals(HabitStatsEffect.NavigateBack, viewModel.effects.first())
        }
}
