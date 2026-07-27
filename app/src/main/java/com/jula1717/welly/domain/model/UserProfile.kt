package com.jula1717.welly.domain.model

data class UserProfile(
    val sex: BiologicalSex,
    val ageYears: Int,
    val heightCm: Double,
    val weightKg: Double,
    val activityLevel: ActivityLevel,
    val goal: NutritionGoal,
)
