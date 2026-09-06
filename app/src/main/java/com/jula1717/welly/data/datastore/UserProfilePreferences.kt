package com.jula1717.welly.data.datastore

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jula1717.welly.domain.model.ActivityLevel
import com.jula1717.welly.domain.model.BiologicalSex
import com.jula1717.welly.domain.model.NutritionGoal
import com.jula1717.welly.domain.model.UserProfile
import java.time.LocalDate

private val SEX = stringPreferencesKey("sex")
private val DATE_OF_BIRTH_EPOCH_DAY = longPreferencesKey("date_of_birth_epoch_day")
private val HEIGHT_CM = intPreferencesKey("height_cm")
private val WEIGHT_KG = doublePreferencesKey("weight_kg")
private val ACTIVITY_LEVEL = stringPreferencesKey("activity_level")
private val GOAL = stringPreferencesKey("goal")

internal fun Preferences.toUserProfile(): UserProfile? {
    val sex = this[SEX]?.let { enumValueOrNull<BiologicalSex>(it) } ?: return null
    val epochDay = this[DATE_OF_BIRTH_EPOCH_DAY] ?: return null
    val heightCm = this[HEIGHT_CM] ?: return null
    val weightKg = this[WEIGHT_KG] ?: return null
    val activityLevel = this[ACTIVITY_LEVEL]?.let { enumValueOrNull<ActivityLevel>(it) } ?: return null
    val goal = this[GOAL]?.let { enumValueOrNull<NutritionGoal>(it) } ?: return null

    val dateOfBirth = runCatching {
        LocalDate.ofEpochDay(epochDay)
    }.getOrNull() ?: return null

    return UserProfile(
        sex = sex,
        dateOfBirth = dateOfBirth,
        heightCm = heightCm,
        weightKg = weightKg,
        activityLevel = activityLevel,
        goal = goal,
    )
}

internal fun MutablePreferences.putUserProfile(profile: UserProfile) {
    this[SEX] = profile.sex.name
    this[DATE_OF_BIRTH_EPOCH_DAY] = profile.dateOfBirth.toEpochDay()
    this[HEIGHT_CM] = profile.heightCm
    this[WEIGHT_KG] = profile.weightKg
    this[ACTIVITY_LEVEL] = profile.activityLevel.name
    this[GOAL] = profile.goal.name
}

private inline fun <reified T : Enum<T>> enumValueOrNull(name: String): T? =
    enumValues<T>().firstOrNull { it.name == name }
