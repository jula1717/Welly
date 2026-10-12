package com.jula1717.welly.presentation.habitstats

sealed interface HabitStatsEffect {
    data object NavigateBack : HabitStatsEffect
}
