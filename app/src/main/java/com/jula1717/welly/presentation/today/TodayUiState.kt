package com.jula1717.welly.presentation.today

import com.jula1717.welly.domain.model.DailyIntakeTotals
import com.jula1717.welly.domain.model.DailyTargets
import java.time.LocalDate

data class TodayUiState(
    val date: LocalDate,
    val maxSelectableDate: LocalDate,
    val targets: DailyTargets,
    val entries: List<TodayEntry> = emptyList(),
    val totals: DailyIntakeTotals = DailyIntakeTotals.EMPTY,
    val canGoToNextDay: Boolean = false,
)
