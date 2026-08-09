package com.jula1717.welly.domain.model

@Suppress("MagicNumber")
enum class NutritionGoal(
    val tdeeFactorMin: Double,
    val tdeeFactorMax: Double,
) {
    Lose(0.80, 0.90),
    Maintain(1.0, 1.0),
    Gain(1.05, 1.15),
}
