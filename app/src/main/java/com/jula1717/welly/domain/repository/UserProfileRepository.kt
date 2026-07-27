package com.jula1717.welly.domain.repository

import com.jula1717.welly.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun observeProfile(): Flow<UserProfile?>

    suspend fun getProfile(): UserProfile?

    suspend fun updateProfile(profile: UserProfile)
}
