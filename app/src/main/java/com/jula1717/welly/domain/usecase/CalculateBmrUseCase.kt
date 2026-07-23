package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BiologicalSex
import javax.inject.Inject
import kotlin.math.roundToInt

class CalculateBmrUseCase
    @Inject
    constructor() {
        operator fun invoke(
            sex: BiologicalSex,
            weightKg: Double,
            heightCm: Double,
            ageYears: Int,
        ): Int {
            val base = (10 * weightKg) + (6.25 * heightCm) - (5 * ageYears)
            val bmr = when (sex) {
                BiologicalSex.Male -> base + 5
                BiologicalSex.Female -> base - 161
            }
            return bmr.roundToInt()
        }
    }
