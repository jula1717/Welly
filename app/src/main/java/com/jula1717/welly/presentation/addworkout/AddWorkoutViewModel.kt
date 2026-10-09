package com.jula1717.welly.presentation.addworkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.AddWorkoutUseCase
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class AddWorkoutViewModel
    @Inject
    constructor(
        getActivitiesUseCase: GetActivitiesUseCase,
        private val addWorkoutUseCase: AddWorkoutUseCase,
        clock: Clock,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(
            LocalDateTime.now(clock).truncatedTo(ChronoUnit.MINUTES).let { now ->
                AddWorkoutUiState(date = now.toLocalDate(), time = now.toLocalTime())
            },
        )
        val uiState: StateFlow<AddWorkoutUiState> = _uiState.asStateFlow()

        private val _effects = Channel<AddWorkoutEffect>()
        val effects: Flow<AddWorkoutEffect> = _effects.receiveAsFlow()

        init {
            viewModelScope.launch {
                getActivitiesUseCase().collect { activities ->
                    _uiState.update { it.copy(activities = activities) }
                }
            }
        }

        fun onEvent(event: AddWorkoutUiEvent) {
            when (event) {
                is AddWorkoutUiEvent.OnDateChanged -> _uiState.update { it.copy(date = event.date) }
                is AddWorkoutUiEvent.OnTimeChanged -> _uiState.update { it.copy(time = event.time) }
                is AddWorkoutUiEvent.OnTypeFilterChanged -> _uiState.update { it.copy(typeFilter = event.type) }
                is AddWorkoutUiEvent.OnActivitySelected ->
                    _uiState.update { it.copy(selectedActivityId = event.activityId) }
                AddWorkoutUiEvent.OnSave -> save()
            }
        }

        private fun save() {
            val activityId = _uiState.value.selectedActivityId
            if (!_uiState.value.canSave || activityId == null) return

            val dateTime = LocalDateTime.of(_uiState.value.date, _uiState.value.time)
            _uiState.update { it.copy(isSaving = true) }

            viewModelScope.launch {
                try {
                    addWorkoutUseCase(activityId, dateTime)
                    _uiState.update { it.copy(isSaving = false) }
                    _effects.send(AddWorkoutEffect.NavigateBack)
                } catch (e: Exception) {
                    Log.e(AddWorkoutViewModel::class.java.name, "Failed to save workout", e)
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }
