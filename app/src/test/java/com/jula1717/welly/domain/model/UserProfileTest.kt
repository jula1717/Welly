package com.jula1717.welly.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class UserProfileTest {
    private fun profile(dateOfBirth: LocalDate) =
        UserProfile(
            sex = BiologicalSex.Unspecified,
            dateOfBirth = dateOfBirth,
            heightCm = 170,
            weightKg = 70.0,
            activityLevel = ActivityLevel.Moderate,
            goal = NutritionGoal.Maintain,
        )

    @Test
    fun `age is completed years since birth`() {
        val result = profile(LocalDate.of(1996, 6, 12)).age(LocalDate.of(2026, 9, 4))
        assertEquals(30, result)
    }

    @Test
    fun `age does not advance until the birthday`() {
        val result = profile(LocalDate.of(1996, 9, 5)).age(LocalDate.of(2026, 9, 4))
        assertEquals(29, result)
    }

    @Test
    fun `age advances on the birthday itself`() {
        val result = profile(LocalDate.of(1996, 9, 4)).age(LocalDate.of(2026, 9, 4))
        assertEquals(30, result)
    }

    @Test
    fun `age is clamped to zero when the birth date is in the future`() {
        val result = profile(LocalDate.of(2030, 1, 1)).age(LocalDate.of(2026, 9, 4))
        assertEquals(0, result)
    }

    @Test
    fun `default profile is age-anchored to the given day`() {
        val today = LocalDate.of(2026, 9, 4)
        assertEquals(30, UserProfile.default(today).age(today))
    }
}
