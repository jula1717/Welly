package com.jula1717.welly.presentation.settings

sealed interface BuiltInHabitsUiEvent {
    data class OnEnabledChanged(
        val habitId: Long,
        val enabled: Boolean,
    ) : BuiltInHabitsUiEvent
}
