package com.jula1717.welly.presentation.habitform

sealed interface HabitFormUiEvent {
    data class OnNameChanged(
        val name: String,
    ) : HabitFormUiEvent

    data class OnPointsChanged(
        val points: Int,
    ) : HabitFormUiEvent

    data class OnFrequencyOptionSelected(
        val option: FrequencyOption,
    ) : HabitFormUiEvent

    data class OnFrequencyValueChanged(
        val field: FrequencyField,
        val value: Int?,
    ) : HabitFormUiEvent

    data object OnSave : HabitFormUiEvent

    data object OnDeleteClicked : HabitFormUiEvent

    data object OnDeleteConfirmed : HabitFormUiEvent

    data object OnDeleteDismissed : HabitFormUiEvent
}
