package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveWorkoutGoalUseCase
    @Inject
    constructor(
        private val observeUserProfile: ObserveUserProfileUseCase,
    ) {
        operator fun invoke(): Flow<Int> =
            observeUserProfile()
                .map { profile -> profile?.workoutsPerWeek ?: UserProfile.DEFAULT_WORKOUTS_PER_WEEK }
                .distinctUntilChanged()
    }
