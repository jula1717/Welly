package com.jula1717.welly.presentation.addmeal

import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.presentation.util.buildMacroPrompt
import java.time.LocalDate
import java.time.LocalTime

data class AddMealUiState(
    val type: MealType = MealType.Breakfast,
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now(),
    val macrosJson: String = "",
    val macros: MealMacros? = null,
    val macrosError: Boolean = false,
    val isSaving: Boolean = false,
) {
    val canSave: Boolean get() = description.isNotBlank() && macros != null

    val generatedPrompt: String
        get() = buildMacroPrompt(description)
}
