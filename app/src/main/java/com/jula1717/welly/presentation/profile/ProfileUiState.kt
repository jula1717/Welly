package com.jula1717.welly.presentation.profile

import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import java.time.LocalDate

data class ProfileUiState(
    val dateOfBirth: LocalDate? = null,
    val heightCm: Int? = null,
    val weightKg: Double? = null,
    val sex: BiologicalSex = BiologicalSex.Unspecified,
    val activityLevel: ActivityLevel? = null,
    val goal: NutritionGoal? = null,
    val isLoaded: Boolean = false,
    val isSaving: Boolean = false,
    val loadFailed: Boolean = false,
    val saveFailed: Boolean = false,
) {
    val canSave: Boolean
        get() = !isSaving && toUserProfile() != null

    fun prefilledFrom(profile: UserProfile): ProfileUiState =
        copy(
            dateOfBirth = profile.dateOfBirth,
            heightCm = profile.heightCm,
            weightKg = profile.weightKg,
            sex = profile.sex,
            activityLevel = profile.activityLevel,
            goal = profile.goal,
        )

    fun toUserProfile(): UserProfile? {
        val birthDate = dateOfBirth ?: return null
        val height = heightCm?.takeIf { it in MIN_HEIGHT_CM..MAX_HEIGHT_CM } ?: return null
        val weight = weightKg?.takeIf { it in MIN_WEIGHT_KG..MAX_WEIGHT_KG } ?: return null
        val activity = activityLevel ?: return null
        val nutritionGoal = goal ?: return null
        return UserProfile(
            sex = sex,
            dateOfBirth = birthDate,
            heightCm = height,
            weightKg = weight,
            activityLevel = activity,
            goal = nutritionGoal,
        )
    }

    companion object {
        // TODO: move this do domain, ValidateUserProfileUseCase
        const val MIN_HEIGHT_CM = 130
        const val MAX_HEIGHT_CM = 220
        const val MIN_WEIGHT_KG = 30.0
        const val MAX_WEIGHT_KG = 250.0

        const val DEFAULT_HEIGHT_CM = 170
        val DEFAULT_DATE_OF_BIRTH: LocalDate = LocalDate.of(2000, 1, 1)

        val EMPTY = ProfileUiState()
    }
}
