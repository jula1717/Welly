package com.jula1717.welly.presentation.adddrink

import java.time.LocalDate
import java.time.LocalTime

sealed interface AddDrinkUiEvent {
    data class OnCalorieTypeChanged(
        val calorieType: DrinkCalorieType,
    ) : AddDrinkUiEvent

    data class OnAmountChanged(
        val amountMl: Int,
    ) : AddDrinkUiEvent

    data class OnDescriptionChanged(
        val description: String,
    ) : AddDrinkUiEvent

    data class OnDateChanged(
        val date: LocalDate,
    ) : AddDrinkUiEvent

    data class OnTimeChanged(
        val time: LocalTime,
    ) : AddDrinkUiEvent

    data class OnMacrosJsonChanged(
        val json: String,
    ) : AddDrinkUiEvent

    data object OnCopyPromptClicked : AddDrinkUiEvent

    data object OnSave : AddDrinkUiEvent
}
