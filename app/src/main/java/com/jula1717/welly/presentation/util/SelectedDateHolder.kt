package com.jula1717.welly.presentation.util

import kotlinx.coroutines.flow.MutableStateFlow
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedDateHolder
    @Inject
    constructor(
        clock: Clock,
    ) {
        val selectedDate = MutableStateFlow(LocalDate.now(clock))
    }
