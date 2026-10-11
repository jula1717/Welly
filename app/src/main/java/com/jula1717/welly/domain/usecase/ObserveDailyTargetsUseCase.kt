package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class ObserveDailyTargetsUseCase
    @Inject
    constructor(
        private val observeUserProfile: ObserveUserProfileUseCase,
        private val calculateDailyTargets: CalculateDailyTargetsUseCase,
        private val clock: Clock,
    ) {
        operator fun invoke(): Flow<DailyTargets> =
            observeUserProfile().map { profile ->
                calculateDailyTargets(profile ?: UserProfile.default(LocalDate.now(clock)))
            }
    }
