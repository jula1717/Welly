package com.jula1717.welly.presentation.adddrink

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.usecase.AddDrinkUseCase
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
class AddDrinkViewModel
    @Inject
    constructor(
        private val addDrinkUseCase: AddDrinkUseCase,
        private val parseMacroJsonUseCase: ParseMacroJsonUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(AddDrinkUiState())
        val uiState: StateFlow<AddDrinkUiState> = _uiState.asStateFlow()

        private val _effects = Channel<AddDrinkEffect>()
        val effects: Flow<AddDrinkEffect> = _effects.receiveAsFlow()

        fun onEvent(event: AddDrinkUiEvent) {
            when (event) {
                is AddDrinkUiEvent.OnCalorieTypeChanged -> _uiState.update { it.copy(calorieType = event.calorieType) }
                is AddDrinkUiEvent.OnAmountChanged -> _uiState.update { it.copy(amountMl = event.amountMl) }
                is AddDrinkUiEvent.OnDescriptionChanged -> _uiState.update { it.copy(description = event.description) }
                is AddDrinkUiEvent.OnDateChanged -> _uiState.update { it.copy(date = event.date) }
                is AddDrinkUiEvent.OnTimeChanged -> _uiState.update { it.copy(time = event.time) }
                is AddDrinkUiEvent.OnMacrosJsonChanged -> parseMacrosJson(event.json)
                AddDrinkUiEvent.OnCopyPromptClicked -> _uiState.update { it.copy(macrosError = false) }
                AddDrinkUiEvent.OnSave -> save()
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
            if (!state.canSave || state.isSaving) return

            _uiState.update { it.copy(isSaving = true) }

            viewModelScope.launch {
                try {
                    addDrinkUseCase(
                        Drink(
                            dateTime = LocalDateTime.of(state.date, state.time),
                            amountMl = state.amountMl,
                            description = state.description,
                            macros = if (state.calorieType == DrinkCalorieType.Caloric) state.macros else null,
                        ),
                    )
                    _uiState.update { it.copy(isSaving = false) }
                    _effects.send(AddDrinkEffect.NavigateBack)
                } catch (e: Exception) {
                    Log.e(AddDrinkViewModel::class.java.name, "Failed to save drink", e)
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }
