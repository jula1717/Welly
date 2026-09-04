package com.jula1717.welly.domain.model

data class DailyIntakeTotals(
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val fiber: Int,
    val hydrationMl: Int,
) {
    companion object {
        val EMPTY = DailyIntakeTotals(calories = 0, protein = 0, carbs = 0, fat = 0, fiber = 0, hydrationMl = 0)
    }
}
