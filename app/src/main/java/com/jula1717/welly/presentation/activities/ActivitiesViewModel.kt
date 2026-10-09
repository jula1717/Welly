package com.jula1717.welly.presentation.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.usecase.AddActivityUseCase
import com.jula1717.welly.domain.usecase.ArchiveActivityUseCase
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import com.jula1717.welly.domain.usecase.UpdateActivityUseCase
import com.jula1717.welly.domain.usecase.ValidateActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel
    @Inject
    constructor(
        getActivitiesUseCase: GetActivitiesUseCase,
        private val addActivityUseCase: AddActivityUseCase,
        private val updateActivityUseCase: UpdateActivityUseCase,
        private val archiveActivityUseCase: ArchiveActivityUseCase,
        private val validateActivityUseCase: ValidateActivityUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ActivitiesUiState())
        val uiState: StateFlow<ActivitiesUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                getActivitiesUseCase().collect { activities ->
                    _uiState.update { it.copy(activities = activities) }
                }
            }
        }

        fun onEvent(event: ActivitiesUiEvent) {
            when (event) {
                ActivitiesUiEvent.OnAddClicked -> openForm(ActivityFormState())
                is ActivitiesUiEvent.OnEditClicked -> openForm(
                    ActivityFormState(
                        id = event.activity.id,
                        name = event.activity.name,
                        type = event.activity.type,
                    ),
                )
                is ActivitiesUiEvent.OnNameChanged -> updateForm {
                    it.copy(name = event.name, isNameValid = validateActivityUseCase(event.name))
                }
                is ActivitiesUiEvent.OnTypeChanged -> updateForm { it.copy(type = event.type) }
                ActivitiesUiEvent.OnFormSave -> saveForm()
                ActivitiesUiEvent.OnFormDismissed -> _uiState.update { it.copy(form = null) }
                ActivitiesUiEvent.OnDeleteClicked -> requestDelete()
                ActivitiesUiEvent.OnDeleteConfirmed -> confirmDelete()
                ActivitiesUiEvent.OnDeleteDismissed -> _uiState.update { it.copy(pendingDelete = null) }
            }
        }

        private fun openForm(form: ActivityFormState) {
            _uiState.update {
                it.copy(form = form.copy(isNameValid = validateActivityUseCase(form.name)))
            }
        }

        private fun updateForm(transform: (ActivityFormState) -> ActivityFormState) {
            _uiState.update { state -> state.copy(form = state.form?.let(transform)) }
        }

        private fun saveForm() {
            val form = _uiState.value.form ?: return
            if (!form.canSave) return

            updateForm { it.copy(isSaving = true) }

            viewModelScope.launch {
                try {
                    val activity = Activity(id = form.id ?: 0L, name = form.name, type = form.type)
                    if (form.isNew) addActivityUseCase(activity) else updateActivityUseCase(activity)
                    _uiState.update { it.copy(form = null) }
                } catch (e: Exception) {
                    Log.e(ActivitiesViewModel::class.java.name, "Failed to save activity", e)
                    updateForm { it.copy(isSaving = false) }
                }
            }
        }

        private fun requestDelete() {
            _uiState.update { state ->
                val activity = state.activities.find { it.id == state.form?.id } ?: return@update state
                state.copy(pendingDelete = activity)
            }
        }

        private fun confirmDelete() {
            val activity = _uiState.value.pendingDelete ?: return
            _uiState.update { it.copy(pendingDelete = null, form = null) }

            viewModelScope.launch {
                try {
                    archiveActivityUseCase(activity.id)
                } catch (e: Exception) {
                    Log.e(ActivitiesViewModel::class.java.name, "Failed to delete activity", e)
                }
            }
        }
    }
