package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserProfileUseCase
    @Inject
    constructor(
        private val repository: UserProfileRepository,
    ) {
        operator fun invoke(): Flow<UserProfile?> = repository.observeProfile()
    }
