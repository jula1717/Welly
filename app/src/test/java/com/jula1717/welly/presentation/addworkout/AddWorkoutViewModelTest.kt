package com.jula1717.welly.presentation.addworkout

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.usecase.AddWorkoutUseCase
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import com.jula1717.welly.fake.FakeActivityRepository
import com.jula1717.welly.fake.FakeWorkoutRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class AddWorkoutViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val now = LocalDateTime.of(2026, 10, 9, 14, 30)
    private val clock = Clock.fixed(now.toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

    private val jogging = Activity(id = 1, name = "Jogging", type = ActivityType.Cardio)
    private val kettlebell = Activity(id = 2, name = "Kettlebell", type = ActivityType.Strength)

    private val workoutRepository = FakeWorkoutRepository()

    private val viewModel by lazy {
        AddWorkoutViewModel(
            getActivitiesUseCase = GetActivitiesUseCase(FakeActivityRepository(listOf(jogging, kettlebell))),
            addWorkoutUseCase = AddWorkoutUseCase(workoutRepository),
            clock = clock,
        )
    }

    @Test
    fun `date and time default to now from the clock`() {
        val state = viewModel.uiState.value

        assertEquals(LocalDate.of(2026, 10, 9), state.date)
        assertEquals(LocalTime.of(14, 30), state.time)
    }

    @Test
    fun `cannot save until an activity is selected`() {
        assertFalse(viewModel.uiState.value.canSave)

        viewModel.onEvent(AddWorkoutUiEvent.OnActivitySelected(jogging.id))

        assertTrue(viewModel.uiState.value.canSave)
    }

    @Test
    fun `type filter narrows the visible activities`() {
        viewModel.onEvent(AddWorkoutUiEvent.OnTypeFilterChanged(ActivityType.Strength))

        assertEquals(listOf(kettlebell), viewModel.uiState.value.visibleActivities)

        viewModel.onEvent(AddWorkoutUiEvent.OnTypeFilterChanged(null))

        assertEquals(listOf(jogging, kettlebell), viewModel.uiState.value.visibleActivities)
    }

    @Test
    fun `selection hidden by the filter cannot be saved`() {
        viewModel.onEvent(AddWorkoutUiEvent.OnActivitySelected(jogging.id))
        viewModel.onEvent(AddWorkoutUiEvent.OnTypeFilterChanged(ActivityType.Strength))

        assertFalse(viewModel.uiState.value.canSave)
    }

    @Test
    fun `save stores the workout with the chosen date and time and navigates back`() =
        runTest {
            val effects = mutableListOf<AddWorkoutEffect>()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.effects.toList(effects) }

            viewModel.onEvent(AddWorkoutUiEvent.OnDateChanged(LocalDate.of(2026, 8, 8)))
            viewModel.onEvent(AddWorkoutUiEvent.OnTimeChanged(LocalTime.of(7, 15)))
            viewModel.onEvent(AddWorkoutUiEvent.OnActivitySelected(kettlebell.id))
            viewModel.onEvent(AddWorkoutUiEvent.OnSave)

            assertEquals(
                listOf(kettlebell.id to LocalDateTime.of(2026, 8, 8, 7, 15)),
                workoutRepository.added,
            )
            assertEquals(listOf(AddWorkoutEffect.NavigateBack), effects)
        }
}
