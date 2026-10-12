package com.jula1717.welly.presentation.habitstats

sealed interface HabitStatsUiEvent {
    data object OnPreviousMonth : HabitStatsUiEvent

    data object OnNextMonth : HabitStatsUiEvent
}
