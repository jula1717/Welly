package com.jula1717.welly.presentation.habitstats

import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitStats
import java.time.LocalDate
import java.time.YearMonth

data class HabitStatsUiState(
    val today: LocalDate,
    val visibleMonth: YearMonth = YearMonth.from(today),
    val habit: Habit? = null,
    val stats: HabitStats? = null,
    val earliestMonth: YearMonth = YearMonth.from(today),
) {
    val canGoToPreviousMonth: Boolean get() = visibleMonth > earliestMonth
    val canGoToNextMonth: Boolean get() = visibleMonth < YearMonth.from(today)
}
