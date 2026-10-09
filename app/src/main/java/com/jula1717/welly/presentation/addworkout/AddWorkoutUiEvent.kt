package com.jula1717.welly.presentation.addworkout

import com.jula1717.welly.domain.model.ActivityType
import java.time.LocalDate
import java.time.LocalTime

sealed interface AddWorkoutUiEvent {
    data class OnDateChanged(
        val date: LocalDate,
    ) : AddWorkoutUiEvent

    data class OnTimeChanged(
        val time: LocalTime,
    ) : AddWorkoutUiEvent

    data class OnTypeFilterChanged(
        val type: ActivityType?,
    ) : AddWorkoutUiEvent

    data class OnActivitySelected(
        val activityId: Long,
    ) : AddWorkoutUiEvent

    data object OnSave : AddWorkoutUiEvent
}
