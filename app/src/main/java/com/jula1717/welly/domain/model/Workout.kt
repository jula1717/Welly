package com.jula1717.welly.domain.model

import java.time.LocalDateTime

data class Workout(
    val id: Long = 0L,
    val activity: Activity,
    val dateTime: LocalDateTime,
)
