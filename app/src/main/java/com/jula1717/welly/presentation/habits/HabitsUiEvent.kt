package com.jula1717.welly.presentation.habits

import java.time.LocalDate

sealed interface HabitsUiEvent {
    data object OnPreviousDay : HabitsUiEvent

    data object OnNextDay : HabitsUiEvent

    data class OnDateSelected(
        val date: LocalDate,
    ) : HabitsUiEvent

    data class OnCountChanged(
        val habitId: Long,
        val count: Int,
    ) : HabitsUiEvent
}
