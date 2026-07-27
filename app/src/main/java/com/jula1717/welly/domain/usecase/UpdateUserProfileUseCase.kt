package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.repository.UserProfileRepository
import javax.inject.Inject

class UpdateUserProfileUseCase
    @Inject
    constructor(
        private val repository: UserProfileRepository,
    ) {
        // TODO: add ValidateUserProfileUseCase
        suspend operator fun invoke(profile: UserProfile) {
            repository.updateProfile(profile)
        }
    }
