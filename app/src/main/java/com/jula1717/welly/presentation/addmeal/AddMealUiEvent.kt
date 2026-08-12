package com.jula1717.welly.presentation.addmeal

import com.jula1717.welly.domain.model.MealType
import java.time.LocalDate
import java.time.LocalTime

sealed interface AddMealUiEvent {
    data class OnTypeChanged(
        val type: MealType,
    ) : AddMealUiEvent

    data class OnDescriptionChanged(
        val description: String,
    ) : AddMealUiEvent

    data class OnDateChanged(
        val date: LocalDate,
    ) : AddMealUiEvent

    data class OnTimeChanged(
        val time: LocalTime,
    ) : AddMealUiEvent

    data class OnMacrosJsonChanged(
        val json: String,
    ) : AddMealUiEvent

    data object OnCopyPromptClicked : AddMealUiEvent

    data object OnSave : AddMealUiEvent
}
