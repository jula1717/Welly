package com.jula1717.welly.presentation.addmeal

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.usecase.AddMealUseCase
import com.jula1717.welly.domain.usecase.ParseMacroJsonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val addMealUseCase: AddMealUseCase,
    private val parseMacroJsonUseCase: ParseMacroJsonUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMealUiState())
    val uiState: StateFlow<AddMealUiState> = _uiState.asStateFlow()

    private val _effects = Channel<AddMealEffect>()
    val effects: Flow<AddMealEffect> = _effects.receiveAsFlow()

    fun onEvent(event: AddMealUiEvent) {
        when (event) {
            is AddMealUiEvent.OnTypeChanged -> _uiState.update { it.copy(type = event.type) }
            is AddMealUiEvent.OnDescriptionChanged -> _uiState.update { it.copy(description = event.description) }
            is AddMealUiEvent.OnDateChanged -> _uiState.update { it.copy(date = event.date) }
            is AddMealUiEvent.OnTimeChanged -> _uiState.update { it.copy(time = event.time) }
            is AddMealUiEvent.OnMacrosJsonChanged -> parseMacrosJson(event.json)
            AddMealUiEvent.OnCopyPromptClicked -> _uiState.update { it.copy(macrosError = false) }
            AddMealUiEvent.OnSave -> save()
        }
    }

    private fun parseMacrosJson(json: String) {
        val result = parseMacroJsonUseCase(json)
        _uiState.update { state ->
            state.copy(
                macrosJson = json,
                macros = result.getOrNull(),
                macrosError = result.isFailure && json.isNotBlank(),
            )
        }
    }

    private fun save() {
        val state = _uiState.value
        val macros = state.macros ?: return
        if (!state.canSave || state.isSaving) return

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            try {
                addMealUseCase(
                    Meal(
                        dateTime = LocalDateTime.of(state.date, state.time),
                        type = state.type,
                        description = state.description,
                        macros = macros,
                    ),
                )
                _uiState.update { it.copy(isSaving = false) }
                _effects.send(AddMealEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(AddMealViewModel::class.java.name, "Failed to save meal", e)
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }
}
