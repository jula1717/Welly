package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.domain.model.UserProfile
import javax.inject.Inject
import kotlin.math.roundToInt

/*
 * - Protein: 1.6–2.2 g/kg
 * - Fat: 25–35% of calorie bounds (9 kcal/g)
 * - Carbs: remaining energy after protein/fat extremes
 * - Fiber: 14 g per 1000 kcal of calorie bounds
 * - Water: 30–40 ml/kg
 */
class CalculateDailyTargetsUseCase
    @Inject
    constructor(
        private val calculateBmr: CalculateBmrUseCase,
    ) {
        operator fun invoke(profile: UserProfile): DailyTargets {
            val bmr =
                calculateBmr(
                    sex = profile.sex,
                    weightKg = profile.weightKg,
                    heightCm = profile.heightCm,
                    ageYears = profile.ageYears,
                )
            val tdee = (bmr * profile.activityLevel.tdeeMultiplier).roundToInt()
            val calorieMin = (tdee * profile.goal.tdeeFactorMin).roundToInt()
            val calorieMax = (tdee * profile.goal.tdeeFactorMax).roundToInt()

            val proteinMin = (profile.weightKg * PROTEIN_G_PER_KG_MIN).roundToInt()
            val proteinMax = (profile.weightKg * PROTEIN_G_PER_KG_MAX).roundToInt()
            val fatMin = ((calorieMin * FAT_CALORIE_FRACTION_MIN) / KCAL_PER_FAT_G).roundToInt()
            val fatMax = ((calorieMax * FAT_CALORIE_FRACTION_MAX) / KCAL_PER_FAT_G).roundToInt()
            val carbsMin =
                remainingCarbsGrams(
                    calorieKcal = calorieMin,
                    proteinGrams = proteinMax,
                    fatGrams = fatMax,
                )
            val carbsMax =
                remainingCarbsGrams(
                    calorieKcal = calorieMax,
                    proteinGrams = proteinMin,
                    fatGrams = fatMin,
                )
            val fiberMin = ((calorieMin / 1000.0) * FIBER_G_PER_1000_KCAL).roundToInt()
            val fiberMax = ((calorieMax / 1000.0) * FIBER_G_PER_1000_KCAL).roundToInt()
            val waterMin = (profile.weightKg * WATER_ML_PER_KG_MIN).roundToInt()
            val waterMax = (profile.weightKg * WATER_ML_PER_KG_MAX).roundToInt()

            return DailyTargets(
                bmrKcal = bmr,
                tdeeKcal = tdee,
                calorieTargetKcalMin = calorieMin,
                calorieTargetKcalMax = calorieMax,
                proteinGramsMin = proteinMin,
                proteinGramsMax = proteinMax,
                carbsGramsMin = carbsMin,
                carbsGramsMax = carbsMax,
                fatGramsMin = fatMin,
                fatGramsMax = fatMax,
                fiberGramsMin = fiberMin,
                fiberGramsMax = fiberMax,
                waterMlMin = waterMin,
                waterMlMax = waterMax,
            )
        }

        private fun remainingCarbsGrams(
            calorieKcal: Int,
            proteinGrams: Int,
            fatGrams: Int,
        ): Int {
            val remainingKcal =
                calorieKcal - (proteinGrams * KCAL_PER_PROTEIN_G) - (fatGrams * KCAL_PER_FAT_G)
            return (remainingKcal.toDouble() / KCAL_PER_CARB_G)
                .coerceAtLeast(0.0)
                .roundToInt()
        }

        private companion object {
            const val PROTEIN_G_PER_KG_MIN = 1.6
            const val PROTEIN_G_PER_KG_MAX = 2.2
            const val FAT_CALORIE_FRACTION_MIN = 0.25
            const val FAT_CALORIE_FRACTION_MAX = 0.35
            const val KCAL_PER_PROTEIN_G = 4
            const val KCAL_PER_CARB_G = 4
            const val KCAL_PER_FAT_G = 9
            const val FIBER_G_PER_1000_KCAL = 14.0
            const val WATER_ML_PER_KG_MIN = 30.0
            const val WATER_ML_PER_KG_MAX = 40.0
        }
    }
