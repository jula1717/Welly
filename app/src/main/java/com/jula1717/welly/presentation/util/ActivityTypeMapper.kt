package com.jula1717.welly.presentation.util

import androidx.annotation.StringRes
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.ActivityType

@get:StringRes
internal val ActivityType.titleResId: Int
    get() = when (this) {
        ActivityType.Cardio -> R.string.activity_type_cardio
        ActivityType.Strength -> R.string.activity_type_strength
    }
