package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.Collator
import java.util.Locale
import javax.inject.Inject

class GetActivitiesUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        operator fun invoke(): Flow<List<Activity>> {
            val collator = Collator.getInstance(Locale.getDefault())
            return repository.getActivities().map { activities ->
                activities.sortedWith(compareBy(collator) { it.name })
            }
        }
    }
