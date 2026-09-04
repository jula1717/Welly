package com.jula1717.welly.presentation.today

import java.time.LocalDate

sealed interface TodayUiEvent {
    data object OnPreviousDay : TodayUiEvent

    data object OnNextDay : TodayUiEvent

    data class OnDateSelected(
        val date: LocalDate,
    ) : TodayUiEvent
}
