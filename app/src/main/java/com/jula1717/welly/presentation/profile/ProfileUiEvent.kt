package com.jula1717.welly.presentation.profile

import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import java.time.LocalDate

sealed interface ProfileUiEvent {
    data class OnDateOfBirthChanged(
        val date: LocalDate,
    ) : ProfileUiEvent

    data class OnHeightChanged(
        val heightCm: Int,
    ) : ProfileUiEvent

    data class OnWeightChanged(
        val rawInput: String,
    ) : ProfileUiEvent

    data class OnSexChanged(
        val sex: BiologicalSex,
    ) : ProfileUiEvent

    data class OnActivityLevelChanged(
        val activityLevel: ActivityLevel,
    ) : ProfileUiEvent

    data class OnGoalChanged(
        val goal: NutritionGoal,
    ) : ProfileUiEvent

    data object OnSave : ProfileUiEvent
}
