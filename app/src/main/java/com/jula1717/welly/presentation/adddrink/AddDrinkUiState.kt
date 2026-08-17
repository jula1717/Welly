package com.jula1717.welly.presentation.adddrink

import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.presentation.util.buildMacroPrompt
import java.time.LocalDate
import java.time.LocalTime

data class AddDrinkUiState(
    val calorieType: DrinkCalorieType = DrinkCalorieType.Caloric,
    val amountMl: Int = 330,
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val macrosJson: String = "",
    val macros: MealMacros? = null,
    val macrosError: Boolean = false,
    val isSaving: Boolean = false,
) {
    val canSave: Boolean
        get() = amountMl > 0 && when (calorieType) {
            DrinkCalorieType.NonCaloric -> true
            DrinkCalorieType.Caloric -> description.isNotBlank() && macros != null
        }

    val generatedPrompt: String
        get() = buildMacroPrompt("a drink ($amountMl ml): $description")

    companion object {
        const val MIN_AMOUNT_ML = 0
        const val MAX_AMOUNT_ML = 5000
    }
}
