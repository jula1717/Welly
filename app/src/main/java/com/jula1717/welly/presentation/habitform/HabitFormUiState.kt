package com.jula1717.welly.presentation.habitform

import com.jula1717.welly.domain.model.BuiltInHabit

data class HabitFormUiState(
    val habitId: Long? = null,
    val builtIn: BuiltInHabit? = null,
    val name: String = "",
    val points: Int = DEFAULT_POINTS,
    val frequency: FrequencyInput = FrequencyInput(),
    val canSave: Boolean = false,
    val quotaTooHigh: Boolean = false,
    val isSaving: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
) {
    val isNew: Boolean get() = habitId == null
    val isBuiltIn: Boolean get() = builtIn != null

    val canDelete: Boolean get() = !isNew && !isBuiltIn

    companion object {
        const val DEFAULT_POINTS = 10
        const val MAX_POINTS = 999
    }
}
