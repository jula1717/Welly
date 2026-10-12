package com.jula1717.welly.presentation.settings

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import com.jula1717.welly.domain.usecase.getHabitsUseCase
import com.jula1717.welly.domain.usecase.testHabit
import com.jula1717.welly.fake.FakeActivityRepository
import com.jula1717.welly.fake.FakeHabitRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val calories = testHabit(id = 1, builtIn = BuiltInHabit.Calories)
    private val workouts = testHabit(id = 2, builtIn = BuiltInHabit.Workouts, isEnabled = false)
    private val custom = testHabit(id = 3)
    private val habits = FakeHabitRepository(listOf(calories, workouts, custom))

    private fun TestScope.buildViewModel(): SettingsViewModel {
        val viewModel = SettingsViewModel(
            getActivitiesUseCase = GetActivitiesUseCase(FakeActivityRepository()),
            getHabitsUseCase = getHabitsUseCase(habits),
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        return viewModel
    }

    @Test
    fun `counts the built-in habits that are switched on and ignores custom ones`() =
        runTest {
            val state = buildViewModel().uiState.value

            assertEquals(2, state.builtInHabitsCount)
            assertEquals(1, state.enabledBuiltInHabits)
        }

    @Test
    fun `follows habits being switched on`() =
        runTest {
            val viewModel = buildViewModel()

            habits.updateHabit(workouts.copy(isEnabled = true))

            assertEquals(2, viewModel.uiState.value.enabledBuiltInHabits)
        }
}
