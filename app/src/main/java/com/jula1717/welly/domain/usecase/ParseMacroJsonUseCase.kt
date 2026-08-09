package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.MealMacros
import org.json.JSONObject
import javax.inject.Inject

class ParseMacroJsonUseCase
    @Inject
    constructor() {
        operator fun invoke(json: String): Result<MealMacros> =
            runCatching {
                val obj = JSONObject(json)
                MealMacros(
                    protein = obj.getInt(KEY_PROTEIN),
                    carbs = obj.getInt(KEY_CARBS),
                    fat = obj.getInt(KEY_FAT),
                    fiber = obj.getInt(KEY_FIBER),
                    calories = obj.getInt(KEY_KCAL),
                )
            }

        companion object {
            const val KEY_PROTEIN = "protein_g"
            const val KEY_CARBS = "carbs_g"
            const val KEY_FAT = "fat_g"
            const val KEY_FIBER = "fiber_g"
            const val KEY_KCAL = "kcal"
        }
    }
