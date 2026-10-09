package com.jula1717.welly.presentation.activities

import com.jula1717.welly.domain.model.ActivityType

data class ActivityFormState(
    val id: Long? = null,
    val name: String = "",
    val type: ActivityType = ActivityType.Cardio,
    val isNameValid: Boolean = false,
    val isSaving: Boolean = false,
) {
    val isNew: Boolean get() = id == null

    val canSave: Boolean get() = isNameValid && !isSaving
}
