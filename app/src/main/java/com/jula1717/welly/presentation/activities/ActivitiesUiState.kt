package com.jula1717.welly.presentation.activities

import com.jula1717.welly.domain.model.Activity

data class ActivitiesUiState(
    val activities: List<Activity> = emptyList(),
    val form: ActivityFormState? = null,
    val pendingDelete: Activity? = null,
)
