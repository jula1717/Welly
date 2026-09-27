package com.jula1717.welly.domain.model

data class Activity(
    val id: Long = 0L,
    val name: String,
    val type: ActivityType,
)
