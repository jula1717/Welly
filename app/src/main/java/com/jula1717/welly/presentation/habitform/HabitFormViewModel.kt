package com.jula1717.welly.presentation.habitform

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.usecase.AddHabitUseCase
import com.jula1717.welly.domain.usecase.DeleteHabitUseCase
import com.jula1717.welly.domain.usecase.GetHabitUseCase
import com.jula1717.welly.domain.usecase.UpdateHabitUseCase
import com.jula1717.welly.domain.usecase.ValidateHabitUseCase
import com.jula1717.welly.presentation.navigation.HabitFormDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitFormViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val getHabit: GetHabitUseCase,
        private val addHabit: AddHabitUseCase,
        private val updateHabit: UpdateHabitUseCase,
        private val deleteHabit: DeleteHabitUseCase,
        private val validate: ValidateHabitUseCase,
    ) : ViewModel() {
        private val habitId: Long? = savedStateHandle[HabitFormDestination.HABIT_ID_KEY]

        private var original: Habit? = null

        private val _uiState = MutableStateFlow(HabitFormUiState(habitId = habitId).validated())
        val uiState: StateFlow<HabitFormUiState> = _uiState.asStateFlow()

        private val _effects = Channel<HabitFormEffect>()
        val effects: Flow<HabitFormEffect> = _effects.receiveAsFlow()

        init {
            habitId?.let { id -> viewModelScope.launch { load(id) } }
        }

        fun onEvent(event: HabitFormUiEvent) {
            when (event) {
                is HabitFormUiEvent.OnNameChanged -> change { it.copy(name = event.name) }
                is HabitFormUiEvent.OnPointsChanged ->
                    change { it.copy(points = event.points.coerceIn(0, HabitFormUiState.MAX_POINTS)) }
                is HabitFormUiEvent.OnFrequencyOptionSelected ->
                    change { it.copy(frequency = it.frequency.copy(option = event.option)) }
                is HabitFormUiEvent.OnFrequencyValueChanged ->
                    change { it.copy(frequency = it.frequency.withValue(event.field, event.value)) }
                HabitFormUiEvent.OnSave -> save()
                HabitFormUiEvent.OnDeleteClicked -> change { it.copy(showDeleteConfirmation = it.canDelete) }
                HabitFormUiEvent.OnDeleteConfirmed -> delete()
                HabitFormUiEvent.OnDeleteDismissed -> change { it.copy(showDeleteConfirmation = false) }
            }
        }

        private suspend fun load(id: Long) {
            val habit = getHabit(id).firstOrNull()
            if (habit == null) {
                _effects.send(HabitFormEffect.NavigateBack)
                return
            }
            original = habit
            change {
                it.copy(
                    builtIn = habit.builtIn,
                    name = habit.name.orEmpty(),
                    points = habit.points,
                    frequency = FrequencyInput.from(habit.frequency),
                )
            }
        }

        private fun save() {
            val state = _uiState.value
            val frequency = state.frequency.toFrequency()
            if (!state.canSave || state.isSaving || frequency == null) return

            change { it.copy(isSaving = true) }
            viewModelScope.launch {
                val habit = original
                if (habit == null) {
                    addHabit(state.name, state.points, frequency)
                } else {
                    updateHabit(habit.copy(name = state.name, points = state.points, frequency = frequency))
                }
                _effects.send(HabitFormEffect.NavigateBack)
            }
        }

        private fun delete() {
            val id = habitId ?: return
            change { it.copy(showDeleteConfirmation = false, isSaving = true) }
            viewModelScope.launch {
                deleteHabit(id)
                _effects.send(HabitFormEffect.NavigateBack)
            }
        }

        private fun change(transform: (HabitFormUiState) -> HabitFormUiState) {
            _uiState.update { transform(it).validated() }
        }

        private fun HabitFormUiState.validated(): HabitFormUiState {
            val frequency = frequency.toFrequency()
            val isBuiltIn = isBuiltIn
            val canSaveValues = frequency != null && validate(name, points, frequency, isBuiltIn)
            val namelessIsValid = frequency != null && validate(PLACEHOLDER_NAME, points, frequency, isBuiltIn)
            return copy(
                canSave = canSaveValues && !isLoadingExisting(),
                quotaTooHigh = frequency != null && !namelessIsValid,
            )
        }

        private fun HabitFormUiState.isLoadingExisting(): Boolean = habitId != null && original == null

        private companion object {
            const val PLACEHOLDER_NAME = "name"
        }
    }
