package com.jula1717.welly.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jula1717.welly.domain.usecase.ObserveUserProfileUseCase
import com.jula1717.welly.domain.usecase.UpdateUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        observeUserProfile: ObserveUserProfileUseCase,
        private val updateUserProfile: UpdateUserProfileUseCase,
        private val clock: Clock,
    ) : ViewModel() {
        val maxDateOfBirth: LocalDate
            get() = LocalDate.now(clock)

        private val _uiState = MutableStateFlow(ProfileUiState.EMPTY)
        val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

        private val _effects = Channel<ProfileEffect>()
        val effects: Flow<ProfileEffect> = _effects.receiveAsFlow()

        init {
            viewModelScope.launch {
                val stored =
                    try {
                        observeUserProfile().first()
                    } catch (e: Exception) {
                        Log.e(ProfileViewModel::class.java.name, "Failed to load profile", e)
                        _uiState.update { it.copy(isLoaded = true, loadFailed = true) }
                        return@launch
                    }
                _uiState.update { state ->
                    (stored?.let(state::prefilledFrom) ?: state).copy(isLoaded = true)
                }
            }
        }

        fun onEvent(event: ProfileUiEvent) {
            when (event) {
                is ProfileUiEvent.OnDateOfBirthChanged ->
                    _uiState.update { it.copy(dateOfBirth = event.date) }

                is ProfileUiEvent.OnHeightChanged ->
                    _uiState.update { it.copy(heightCm = event.heightCm) }

                is ProfileUiEvent.OnWeightChanged ->
                    _uiState.update { it.copy(weightKg = parseWeight(event.rawInput)) }

                is ProfileUiEvent.OnSexChanged ->
                    _uiState.update { it.copy(sex = event.sex) }

                is ProfileUiEvent.OnActivityLevelChanged ->
                    _uiState.update { it.copy(activityLevel = event.activityLevel) }

                is ProfileUiEvent.OnGoalChanged ->
                    _uiState.update { it.copy(goal = event.goal) }

                ProfileUiEvent.OnSave -> save()
            }
        }

        private fun save() {
            val state = _uiState.value
            if (!state.canSave) return
            val profile = state.toUserProfile() ?: return

            _uiState.update { it.copy(isSaving = true, saveFailed = false) }

            viewModelScope.launch {
                try {
                    updateUserProfile(profile)
                    _uiState.update { it.copy(isSaving = false) }
                    _effects.send(ProfileEffect.NavigateBack)
                } catch (e: Exception) {
                    Log.e(ProfileViewModel::class.java.name, "Failed to save profile", e)
                    _uiState.update { it.copy(isSaving = false, saveFailed = true) }
                }
            }
        }
    }
