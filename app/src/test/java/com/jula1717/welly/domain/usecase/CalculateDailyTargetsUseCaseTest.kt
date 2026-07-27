package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculateDailyTargetsUseCaseTest {
    private val useCase = CalculateDailyTargetsUseCase(CalculateBmrUseCase())

    @Test
    fun `calculates target ranges for maintain goal`() {
        val profile =
            UserProfile(
                sex = BiologicalSex.Female,
                ageYears = 30,
                heightCm = 165.0,
                weightKg = 60.0,
                activityLevel = ActivityLevel.Moderate,
                goal = NutritionGoal.Maintain,
            )

        val targets = useCase(profile)

        assertEquals(1320, targets.bmrKcal)
        assertEquals(2046, targets.tdeeKcal)
        assertEquals(2046, targets.calorieTargetKcalMin)
        assertEquals(2046, targets.calorieTargetKcalMax)
        assertEquals(96, targets.proteinGramsMin)
        assertEquals(132, targets.proteinGramsMax)
        assertEquals(57, targets.fatGramsMin)
        assertEquals(80, targets.fatGramsMax)
        assertEquals(200, targets.carbsGramsMin)
        assertEquals(287, targets.carbsGramsMax)
        assertEquals(29, targets.fiberGramsMin)
        assertEquals(29, targets.fiberGramsMax)
        assertEquals(1800, targets.waterMlMin)
        assertEquals(2400, targets.waterMlMax)
    }

    @Test
    fun `calculates target ranges for lose goal`() {
        val profile =
            UserProfile(
                sex = BiologicalSex.Female,
                ageYears = 30,
                heightCm = 165.0,
                weightKg = 60.0,
                activityLevel = ActivityLevel.Moderate,
                goal = NutritionGoal.Lose,
            )
        val targets = useCase(profile)

        assertEquals(2046, targets.tdeeKcal)
        assertEquals(1637, targets.calorieTargetKcalMin)
        assertEquals(1841, targets.calorieTargetKcalMax)
    }

    @Test
    fun `calculates target ranges for gain goal`() {
        val profile =
            UserProfile(
                sex = BiologicalSex.Male,
                ageYears = 28,
                heightCm = 180.0,
                weightKg = 80.0,
                activityLevel = ActivityLevel.Active,
                goal = NutritionGoal.Gain,
            )
        val targets = useCase(profile)

        assertEquals(1790, targets.bmrKcal)
        assertEquals(3088, targets.tdeeKcal)
        assertEquals(3242, targets.calorieTargetKcalMin)
        assertEquals(3551, targets.calorieTargetKcalMax)
        assertEquals(128, targets.proteinGramsMin)
        assertEquals(176, targets.proteinGramsMax)
    }
}
