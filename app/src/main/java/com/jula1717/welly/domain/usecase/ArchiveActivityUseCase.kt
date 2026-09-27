package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.repository.ActivityRepository
import javax.inject.Inject

class ArchiveActivityUseCase
    @Inject
    constructor(
        private val repository: ActivityRepository,
    ) {
        suspend operator fun invoke(id: Long) = repository.archiveActivity(id)
    }
