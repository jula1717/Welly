package com.jula1717.welly.presentation.addmeal

import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
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

    // TODO: temporary hardcoded prompt template - will be replaced with a proper prompt builder later.
    val generatedPrompt: String
        get() = "Calculate macros for: $description\n" +
            "Respond only in JSON:\n" +
            "{\"protein_g\": 0, \"carbs_g\": 0, \"fat_g\": 0, \"fiber_g\": 0, \"kcal\": 0}"
}
