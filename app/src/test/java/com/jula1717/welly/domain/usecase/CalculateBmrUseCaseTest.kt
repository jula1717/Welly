package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BiologicalSex
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateBmrUseCaseTest {
    private val useCase = CalculateBmrUseCase()

    @Test
    fun `calculates bmr for female`() {
        val result = useCase(
            sex = BiologicalSex.Female,
            weightKg = 60.0,
            heightCm = 165,
            ageYears = 30,
        )
        assertEquals(1320, result)
    }

    @Test
    fun `calculates bmr for male`() {
        val result = useCase(
            sex = BiologicalSex.Male,
            weightKg = 80.0,
            heightCm = 180,
            ageYears = 28,
        )
        assertEquals(1790, result)
    }

    @Test
    fun `calculates bmr for unspecified sex as the average of male and female`() {
        val result = useCase(
            sex = BiologicalSex.Unspecified,
            weightKg = 60.0,
            heightCm = 165,
            ageYears = 30,
        )
        assertEquals(1403, result)
    }
}
