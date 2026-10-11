package com.jula1717.welly.domain.model

import java.time.LocalDate

/**
 * [builtIn] is non-null for habits shipped with the app; they are tracked automatically from logged
 * data and can only be disabled, never deleted. A null [name] means "use the localized default".
 */
data class Habit(
    val id: Long = 0L,
    val builtIn: BuiltInHabit? = null,
    val name: String?,
    val points: Int,
    val frequency: HabitFrequency,
    val isEnabled: Boolean = true,
    val startDate: LocalDate,
) {
    val isBuiltIn: Boolean get() = builtIn != null
}
