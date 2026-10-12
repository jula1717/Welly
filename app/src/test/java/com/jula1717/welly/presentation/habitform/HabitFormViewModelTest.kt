package com.jula1717.welly.presentation.habitform

import androidx.lifecycle.SavedStateHandle
import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.HabitPeriodUnit
import com.jula1717.welly.domain.usecase.AddHabitUseCase
import com.jula1717.welly.domain.usecase.DeleteHabitUseCase
import com.jula1717.welly.domain.usecase.HABIT_TODAY
import com.jula1717.welly.domain.usecase.UpdateHabitUseCase
import com.jula1717.welly.domain.usecase.ValidateHabitUseCase
import com.jula1717.welly.domain.usecase.getHabitUseCase
import com.jula1717.welly.domain.usecase.habitClock
import com.jula1717.welly.domain.usecase.testHabit
import com.jula1717.welly.fake.FakeHabitRepository
import com.jula1717.welly.presentation.navigation.HabitFormDestination
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HabitFormViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val custom = testHabit(id = 1).copy(name = "Reading", points = 20)
    private val builtIn = testHabit(id = 2, builtIn = BuiltInHabit.Workouts, frequency = HabitFrequency.Weekly)
    private val repository = FakeHabitRepository(listOf(custom, builtIn))

    private fun viewModel(habitId: Long? = null): HabitFormViewModel {
        val arguments = habitId?.let { mapOf(HabitFormDestination.HABIT_ID_KEY to it) }.orEmpty()
        return HabitFormViewModel(
            savedStateHandle = SavedStateHandle(arguments),
            getHabit = getHabitUseCase(repository),
            addHabit = AddHabitUseCase(repository, ValidateHabitUseCase(), habitClock()),
            updateHabit = UpdateHabitUseCase(repository, ValidateHabitUseCase()),
            deleteHabit = DeleteHabitUseCase(repository),
            validate = ValidateHabitUseCase(),
        )
    }

    @Test
    fun `a new form starts empty and cannot be saved`() {
        val state = viewModel().uiState.value

        assertTrue(state.isNew)
        assertEquals("", state.name)
        assertEquals(HabitFormUiState.DEFAULT_POINTS, state.points)
        assertEquals(FrequencyOption.Daily, state.frequency.option)
        assertFalse(state.canSave)
    }

    @Test
    fun `a name makes a daily habit saveable`() {
        val viewModel = viewModel()

        viewModel.onEvent(HabitFormUiEvent.OnNameChanged("Stretching"))

        assertTrue(viewModel.uiState.value.canSave)
    }

    @Test
    fun `a name that starts with a space cannot be saved`() {
        val viewModel = viewModel()

        viewModel.onEvent(HabitFormUiEvent.OnNameChanged(" Stretching"))

        assertFalse(viewModel.uiState.value.canSave)
    }

    @Test
    fun `saving a new habit adds it and leaves the form`() =
        runTest {
            val viewModel = viewModel()
            viewModel.onEvent(HabitFormUiEvent.OnNameChanged("Stretching "))
            viewModel.onEvent(HabitFormUiEvent.OnPointsChanged(5))
            viewModel.onEvent(HabitFormUiEvent.OnFrequencyValueChanged(FrequencyField.TimesPerWeek, 3))

            viewModel.onEvent(HabitFormUiEvent.OnSave)

            assertEquals(HabitFormEffect.NavigateBack, viewModel.effects.first())
            val added = repository.getHabits().first().last()
            assertEquals("Stretching", added.name)
            assertEquals(5, added.points)
            assertEquals(HabitFrequency(3, 1, HabitPeriodUnit.Week), added.frequency)
            assertEquals(HABIT_TODAY, added.startDate)
        }

    @Test
    fun `typing into a row selects it`() {
        val viewModel = viewModel()

        viewModel.onEvent(HabitFormUiEvent.OnFrequencyValueChanged(FrequencyField.EveryDays, 3))

        assertEquals(FrequencyOption.EveryNDays, viewModel.uiState.value.frequency.option)
    }

    @Test
    fun `the rows of the frequency form do not share their numbers`() {
        val viewModel = viewModel()

        viewModel.onEvent(HabitFormUiEvent.OnFrequencyValueChanged(FrequencyField.TimesPerDay, 5))
        viewModel.onEvent(HabitFormUiEvent.OnFrequencyValueChanged(FrequencyField.EveryDays, 3))

        val input = viewModel.uiState.value.frequency
        assertEquals(5, input.valueOf(FrequencyField.TimesPerDay))
        assertEquals(3, input.valueOf(FrequencyField.EveryDays))
        assertEquals(null, input.valueOf(FrequencyField.TimesPerWeek))
        assertEquals(null, input.valueOf(FrequencyField.TimesInDays))
        assertEquals(null, input.valueOf(FrequencyField.DaysWindow))
    }

    @Test
    fun `an unfinished frequency blocks saving`() {
        val viewModel = viewModel()
        viewModel.onEvent(HabitFormUiEvent.OnNameChanged("Stretching"))

        viewModel.onEvent(HabitFormUiEvent.OnFrequencyOptionSelected(FrequencyOption.TimesPerWeek))

        assertFalse(viewModel.uiState.value.canSave)
        assertFalse(viewModel.uiState.value.quotaTooHigh)
    }

    @Test
    fun `a quota that does not fit its period is flagged and blocks saving`() {
        val viewModel = viewModel()
        viewModel.onEvent(HabitFormUiEvent.OnNameChanged("Stretching"))

        viewModel.onEvent(HabitFormUiEvent.OnFrequencyValueChanged(FrequencyField.TimesPerWeek, 8))

        assertTrue(viewModel.uiState.value.quotaTooHigh)
        assertFalse(viewModel.uiState.value.canSave)
    }

    @Test
    fun `points stay within their limits`() {
        val viewModel = viewModel()

        viewModel.onEvent(HabitFormUiEvent.OnPointsChanged(-4))
        assertEquals(0, viewModel.uiState.value.points)

        viewModel.onEvent(HabitFormUiEvent.OnPointsChanged(HabitFormUiState.MAX_POINTS + 1))
        assertEquals(HabitFormUiState.MAX_POINTS, viewModel.uiState.value.points)
    }

    @Test
    fun `editing loads the habit and saves changes to the same habit`() =
        runTest {
            val viewModel = viewModel(custom.id)
            val loaded = viewModel.uiState.value
            assertFalse(loaded.isNew)
            assertEquals("Reading", loaded.name)
            assertEquals(20, loaded.points)
            assertTrue(loaded.canSave)

            viewModel.onEvent(HabitFormUiEvent.OnNameChanged("Reading more"))
            viewModel.onEvent(HabitFormUiEvent.OnSave)

            assertEquals(HabitFormEffect.NavigateBack, viewModel.effects.first())
            val updated = repository.getHabits().first().first { it.id == custom.id }
            assertEquals("Reading more", updated.name)
            assertEquals(custom.startDate, updated.startDate)
        }

    @Test
    fun `leaves when the habit to edit does not exist`() =
        runTest {
            assertEquals(HabitFormEffect.NavigateBack, viewModel(99).effects.first())
        }

    @Test
    fun `a built-in habit keeps its schedule and name, only its points can change and it cannot be deleted`() =
        runTest {
            val viewModel = viewModel(builtIn.id)
            val state = viewModel.uiState.value
            assertTrue(state.isBuiltIn)
            assertFalse(state.canDelete)
            assertEquals(FrequencyOption.TimesPerWeek, state.frequency.option)
            assertTrue(state.canSave)

            viewModel.onEvent(HabitFormUiEvent.OnDeleteClicked)
            assertFalse(viewModel.uiState.value.showDeleteConfirmation)

            viewModel.onEvent(HabitFormUiEvent.OnPointsChanged(25))
            viewModel.onEvent(HabitFormUiEvent.OnSave)
            assertEquals(HabitFormEffect.NavigateBack, viewModel.effects.first())
            val saved = repository.getHabits().first().first { it.id == builtIn.id }
            assertEquals(25, saved.points)
            assertEquals(null, saved.name)
            assertEquals(HabitFrequency.Weekly, saved.frequency)
        }

    @Test
    fun `deleting a custom habit asks first and then removes it`() =
        runTest {
            val viewModel = viewModel(custom.id)

            viewModel.onEvent(HabitFormUiEvent.OnDeleteClicked)
            assertTrue(viewModel.uiState.value.showDeleteConfirmation)
            viewModel.onEvent(HabitFormUiEvent.OnDeleteDismissed)
            assertFalse(viewModel.uiState.value.showDeleteConfirmation)
            assertTrue(repository.deletedIds.isEmpty())

            viewModel.onEvent(HabitFormUiEvent.OnDeleteClicked)
            viewModel.onEvent(HabitFormUiEvent.OnDeleteConfirmed)

            assertEquals(HabitFormEffect.NavigateBack, viewModel.effects.first())
            assertEquals(listOf(custom.id), repository.deletedIds)
        }
}
