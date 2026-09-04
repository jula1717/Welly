package com.jula1717.welly.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R

internal enum class RangeStatus(
    val icon: ImageVector,
    @get:StringRes val contentDescriptionRes: Int,
) {
    Below(Icons.Default.ArrowDownward, R.string.cd_range_below),
    Within(Icons.Default.Check, R.string.cd_range_within),
    Above(Icons.Default.ArrowUpward, R.string.cd_range_above),
}

internal fun rangeStatus(
    value: Int,
    min: Int,
    max: Int,
): RangeStatus = when {
    value < min -> RangeStatus.Below
    value > max -> RangeStatus.Above
    else -> RangeStatus.Within
}
