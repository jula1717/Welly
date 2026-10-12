package com.jula1717.welly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import com.jula1717.welly.domain.usecase.GetHabitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        getActivitiesUseCase: GetActivitiesUseCase,
        getHabitsUseCase: GetHabitsUseCase,
    ) : ViewModel() {
        val uiState: StateFlow<SettingsUiState> =
            combine(getActivitiesUseCase(), getHabitsUseCase()) { activities, habits ->
                val builtIn = habits.filter { it.isBuiltIn }
                SettingsUiState(
                    activityCount = activities.size,
                    enabledBuiltInHabits = builtIn.count { it.isEnabled },
                    builtInHabitsCount = builtIn.size,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                initialValue = SettingsUiState(),
            )

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
