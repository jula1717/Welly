package com.jula1717.welly.presentation.addworkout

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import java.time.LocalDate
import java.time.LocalTime

data class AddWorkoutUiState(
    val date: LocalDate,
    val time: LocalTime,
    val typeFilter: ActivityType? = null,
    val activities: List<Activity> = emptyList(),
    val selectedActivityId: Long? = null,
    val isSaving: Boolean = false,
) {
    val visibleActivities: List<Activity>
        get() = if (typeFilter == null) activities else activities.filter { it.type == typeFilter }

    val canSave: Boolean
        get() = !isSaving && visibleActivities.any { it.id == selectedActivityId }
}
