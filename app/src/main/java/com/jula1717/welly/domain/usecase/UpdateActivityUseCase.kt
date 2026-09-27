package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.repository.ActivityRepository
import javax.inject.Inject

class UpdateActivityUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(activity: Activity) =
            repository.updateActivity(activity.copy(name = activity.name.trimEnd()))
    }
