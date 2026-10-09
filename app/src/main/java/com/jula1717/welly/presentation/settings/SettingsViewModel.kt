package com.jula1717.welly.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.GetActivitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(
        getActivitiesUseCase: GetActivitiesUseCase,
    ) : ViewModel() {
        val uiState: StateFlow<SettingsUiState> =
            getActivitiesUseCase()
                .map { activities -> SettingsUiState(activityCount = activities.size) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
                    initialValue = SettingsUiState(),
                )

        private companion object {
            const val STOP_TIMEOUT_MS = 5_000L
        }
    }
