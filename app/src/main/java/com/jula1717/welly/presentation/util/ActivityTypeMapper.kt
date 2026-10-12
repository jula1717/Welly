package com.jula1717.welly.presentation.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityType

@get:StringRes
internal val ActivityType.titleResId: Int
    get() = when (this) {
        ActivityType.Cardio -> R.string.activity_type_cardio
        ActivityType.Strength -> R.string.activity_type_strength
    }

internal val ActivityType.icon: ImageVector
    get() = when (this) {
        ActivityType.Cardio -> Icons.Outlined.DirectionsRun
        ActivityType.Strength -> Icons.Outlined.FitnessCenter
    }
