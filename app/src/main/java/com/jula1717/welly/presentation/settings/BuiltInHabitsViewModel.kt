package com.jula1717.welly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.GetHabitsUseCase
import com.jula1717.welly.domain.usecase.SetHabitEnabledUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BuiltInHabitsViewModel
    @Inject
    constructor(
        getHabitsUseCase: GetHabitsUseCase,
        private val setHabitEnabledUseCase: SetHabitEnabledUseCase,
    ) : ViewModel() {
        val uiState: StateFlow<BuiltInHabitsUiState> =
            getHabitsUseCase()
                .map { habits ->
                    BuiltInHabitsUiState(
                        habits = habits.mapNotNull { habit ->
                            habit.builtIn?.let { type ->
                                BuiltInHabitSetting(
                                    id = habit.id,
                                    type = type,
                                    frequency = habit.frequency,
                                    isEnabled = habit.isEnabled,
                                )
                            }
                        },
                    )
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                    initialValue = BuiltInHabitsUiState(),
                )

        fun onEvent(event: BuiltInHabitsUiEvent) {
            when (event) {
                is BuiltInHabitsUiEvent.OnEnabledChanged ->
                    viewModelScope.launch { setHabitEnabledUseCase(event.habitId, event.enabled) }
            }
        }

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
