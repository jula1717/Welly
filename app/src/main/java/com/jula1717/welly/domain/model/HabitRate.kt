package com.jula1717.welly.domain.model

import kotlin.math.roundToInt

data class HabitRate(
    val met: Int,
    val total: Int,
) {
    val percent: Int get() = if (total == 0) 0 else (met * PERCENT / total.toDouble()).roundToInt()

    private companion object {
        const val PERCENT = 100
    }
}
