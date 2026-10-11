package com.jula1717.welly.fake

import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserProfileRepository(
    private val profile: UserProfile? = null,
) : UserProfileRepository {
    override fun observeProfile(): Flow<UserProfile?> = flowOf(profile)

    override suspend fun updateProfile(profile: UserProfile) = Unit
}
