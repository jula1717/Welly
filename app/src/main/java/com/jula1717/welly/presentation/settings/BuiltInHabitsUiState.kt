package com.jula1717.welly.presentation.settings

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.HabitFrequency

data class BuiltInHabitsUiState(
    val habits: List<BuiltInHabitSetting> = emptyList(),
)

/** A built-in habit that can be switched on or off. */
data class BuiltInHabitSetting(
    val id: Long,
    val type: BuiltInHabit,
    val frequency: HabitFrequency,
    val isEnabled: Boolean,
)
