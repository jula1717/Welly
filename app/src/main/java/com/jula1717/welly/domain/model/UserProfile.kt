package com.jula1717.welly.domain.model

import java.time.LocalDate
import java.time.Period

data class UserProfile(
    val sex: BiologicalSex,
    val dateOfBirth: LocalDate,
    val heightCm: Int,
    val weightKg: Double,
    val activityLevel: ActivityLevel,
    val goal: NutritionGoal,
) {
    fun age(date: LocalDate): Int = Period.between(dateOfBirth, date).years.coerceAtLeast(0)

    companion object {
        fun default(today: LocalDate): UserProfile =
            UserProfile(
                sex = BiologicalSex.Female,
                dateOfBirth = today.minusYears(DEFAULT_AGE_YEARS),
                heightCm = DEFAULT_HEIGHT_CM,
                weightKg = DEFAULT_WEIGHT_KG,
                activityLevel = ActivityLevel.Moderate,
                goal = NutritionGoal.Maintain,
            )

        private const val DEFAULT_AGE_YEARS = 30L
        private const val DEFAULT_HEIGHT_CM = 180
        private const val DEFAULT_WEIGHT_KG = 70.0
    }
}
