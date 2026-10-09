package com.jula1717.welly.presentation.activities

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType

sealed interface ActivitiesUiEvent {
    data object OnAddClicked : ActivitiesUiEvent

    data class OnEditClicked(
        val activity: Activity,
    ) : ActivitiesUiEvent

    data class OnNameChanged(
        val name: String,
    ) : ActivitiesUiEvent

    data class OnTypeChanged(
        val type: ActivityType,
    ) : ActivitiesUiEvent

    data object OnFormSave : ActivitiesUiEvent

    data object OnFormDismissed : ActivitiesUiEvent

    /** Delete button in the form sheet; targets the activity currently being edited. */
    data object OnDeleteClicked : ActivitiesUiEvent

    data object OnDeleteConfirmed : ActivitiesUiEvent

    data object OnDeleteDismissed : ActivitiesUiEvent
}
