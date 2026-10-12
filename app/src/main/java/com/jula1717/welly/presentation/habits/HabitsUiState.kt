package com.jula1717.welly.presentation.habits

import com.jula1717.welly.domain.model.HabitDaySummary
import java.time.LocalDate

data class HabitsUiState(
    val date: LocalDate,
    val maxSelectableDate: LocalDate,
    val canGoToNextDay: Boolean = false,
    val habits: List<HabitDaySummary> = emptyList(),
)
