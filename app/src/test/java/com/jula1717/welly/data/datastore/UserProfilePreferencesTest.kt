package com.jula1717.welly.data.datastore

import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class UserProfilePreferencesTest {
    private val profile = UserProfile(
        sex = BiologicalSex.Unspecified,
        dateOfBirth = LocalDate.of(1996, 6, 12),
        heightCm = 168,
        weightKg = 61.0,
        activityLevel = ActivityLevel.Light,
        goal = NutritionGoal.Lose,
    )

    @Test
    fun `save and restore a profile`() {
        val prefs = mutablePreferencesOf().apply { putUserProfile(profile) }

        assertEquals(profile, prefs.toUserProfile())
    }

    @Test
    fun `empty preferences produce no profile`() {
        assertNull(emptyPreferences().toUserProfile())
    }
}
