package com.jula1717.welly.presentation.activities

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.usecase.AddActivityUseCase
import com.jula1717.welly.domain.usecase.ArchiveActivityUseCase
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import com.jula1717.welly.domain.usecase.UpdateActivityUseCase
import com.jula1717.welly.domain.usecase.ValidateActivityUseCase
import com.jula1717.welly.fake.FakeActivityRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ActivitiesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val jogging = Activity(id = 1, name = "Jogging", type = ActivityType.Cardio)
    private val yoga = Activity(id = 2, name = "Yoga", type = ActivityType.Strength)

    private val repository = FakeActivityRepository(listOf(jogging, yoga))

    private val viewModel by lazy {
        ActivitiesViewModel(
            getActivitiesUseCase = GetActivitiesUseCase(repository),
            addActivityUseCase = AddActivityUseCase(repository),
            updateActivityUseCase = UpdateActivityUseCase(repository),
            archiveActivityUseCase = ArchiveActivityUseCase(repository),
            validateActivityUseCase = ValidateActivityUseCase(),
        )
    }

    @Test
    fun `lists non-archived activities`() {
        assertEquals(listOf(jogging, yoga), viewModel.uiState.value.activities)
    }

    @Test
    fun `add opens an empty form that cannot be saved yet`() {
        viewModel.onEvent(ActivitiesUiEvent.OnAddClicked)

        val form = currentForm()
        assertTrue(form.isNew)
        assertEquals("", form.name)
        assertFalse(form.canSave)
    }

    @Test
    fun `saving a new activity adds it and closes the form`() {
        viewModel.onEvent(ActivitiesUiEvent.OnAddClicked)
        viewModel.onEvent(ActivitiesUiEvent.OnNameChanged("Swimming "))
        viewModel.onEvent(ActivitiesUiEvent.OnTypeChanged(ActivityType.Cardio))
        viewModel.onEvent(ActivitiesUiEvent.OnFormSave)

        assertEquals(listOf(Activity(name = "Swimming", type = ActivityType.Cardio)), repository.added)
        assertNull(viewModel.uiState.value.form)
    }

    @Test
    fun `blank name blocks saving`() {
        viewModel.onEvent(ActivitiesUiEvent.OnAddClicked)
        viewModel.onEvent(ActivitiesUiEvent.OnNameChanged("   "))
        viewModel.onEvent(ActivitiesUiEvent.OnFormSave)

        assertTrue(repository.added.isEmpty())
        val form = currentForm()
        assertFalse(form.canSave)
    }

    @Test
    fun `editing fills the form and saves an update with the same id`() {
        viewModel.onEvent(ActivitiesUiEvent.OnEditClicked(jogging))

        val form = currentForm()
        assertEquals(jogging.id, form.id)
        assertEquals("Jogging", form.name)
        assertTrue(form.canSave)

        viewModel.onEvent(ActivitiesUiEvent.OnNameChanged("Trail running"))
        viewModel.onEvent(ActivitiesUiEvent.OnFormSave)

        assertEquals(listOf(jogging.copy(name = "Trail running")), repository.updated)
        assertTrue(repository.added.isEmpty())
        assertNull(viewModel.uiState.value.form)
    }

    @Test
    fun `dismissing the form discards changes`() {
        viewModel.onEvent(ActivitiesUiEvent.OnEditClicked(jogging))
        viewModel.onEvent(ActivitiesUiEvent.OnNameChanged("New name"))
        viewModel.onEvent(ActivitiesUiEvent.OnFormDismissed)

        assertNull(viewModel.uiState.value.form)
        assertTrue(repository.updated.isEmpty())
    }

    @Test
    fun `delete from the edit form archives only after confirmation and closes the form`() {
        viewModel.onEvent(ActivitiesUiEvent.OnEditClicked(yoga))
        viewModel.onEvent(ActivitiesUiEvent.OnDeleteClicked)

        assertEquals(yoga, viewModel.uiState.value.pendingDelete)
        assertTrue(repository.archivedIds.isEmpty())

        viewModel.onEvent(ActivitiesUiEvent.OnDeleteConfirmed)

        assertEquals(listOf(yoga.id), repository.archivedIds)
        assertNull(viewModel.uiState.value.pendingDelete)
        assertNull(viewModel.uiState.value.form)
        assertEquals(listOf(jogging), viewModel.uiState.value.activities)
    }

    @Test
    fun `dismissing delete keeps the activity and returns to the form`() {
        viewModel.onEvent(ActivitiesUiEvent.OnEditClicked(yoga))
        viewModel.onEvent(ActivitiesUiEvent.OnDeleteClicked)
        viewModel.onEvent(ActivitiesUiEvent.OnDeleteDismissed)

        assertNull(viewModel.uiState.value.pendingDelete)
        assertTrue(repository.archivedIds.isEmpty())
        assertEquals(yoga.id, currentForm().id)
    }

    @Test
    fun `delete is ignored while adding a new activity`() {
        viewModel.onEvent(ActivitiesUiEvent.OnAddClicked)
        viewModel.onEvent(ActivitiesUiEvent.OnDeleteClicked)

        assertNull(viewModel.uiState.value.pendingDelete)
    }

    private fun currentForm(): ActivityFormState = requireNotNull(viewModel.uiState.value.form) { "Form is not open" }
}
