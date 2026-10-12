package com.jula1717.welly.presentation.settings

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.usecase.SetHabitEnabledUseCase
import com.jula1717.welly.domain.usecase.getHabitsUseCase
import com.jula1717.welly.domain.usecase.testHabit
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BuiltInHabitsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val calories = testHabit(id = 1, builtIn = BuiltInHabit.Calories)
    private val workouts = testHabit(id = 2, builtIn = BuiltInHabit.Workouts)
    private val custom = testHabit(id = 3)
    private val habits = FakeHabitRepository(listOf(calories, workouts, custom))

    private fun TestScope.buildViewModel(): BuiltInHabitsViewModel {
        val viewModel = BuiltInHabitsViewModel(
            getHabitsUseCase = getHabitsUseCase(habits),
            setHabitEnabledUseCase = SetHabitEnabledUseCase(habits),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        return viewModel
    }

    private fun BuiltInHabitsViewModel.settingOf(habitId: Long): BuiltInHabitSetting {
        val settings = uiState.value.habits
        return settings.first { it.id == habitId }
    }

    @Test
    fun `lists only the built-in habits`() =
        runTest {
            val state = buildViewModel().uiState.value

            assertEquals(listOf(BuiltInHabit.Calories, BuiltInHabit.Workouts), state.habits.map { it.type })
        }

    @Test
    fun `switching a built-in habit off and on is reflected in the list`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(BuiltInHabitsUiEvent.OnEnabledChanged(workouts.id, enabled = false))
            val off = viewModel.settingOf(workouts.id)
            viewModel.onEvent(BuiltInHabitsUiEvent.OnEnabledChanged(workouts.id, enabled = true))
            val on = viewModel.settingOf(workouts.id)

            assertEquals(false, off.isEnabled)
            assertEquals(true, on.isEnabled)
        }
}
