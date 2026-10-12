package com.jula1717.welly.presentation.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.DonutLarge
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R
import com.jula1717.welly.domain.model.BuiltInHabit

@get:StringRes
internal val BuiltInHabit.nameResId: Int
    get() = when (this) {
        BuiltInHabit.Macros -> R.string.habit_built_in_macros
        BuiltInHabit.Calories -> R.string.habit_built_in_calories
        BuiltInHabit.Hydration -> R.string.habit_built_in_hydration
        BuiltInHabit.Workouts -> R.string.habit_built_in_workouts
    }

internal val BuiltInHabit.icon: ImageVector
    get() = when (this) {
        BuiltInHabit.Macros -> Icons.Outlined.DonutLarge
        BuiltInHabit.Calories -> Icons.Outlined.Bolt
        BuiltInHabit.Hydration -> Icons.Outlined.WaterDrop
        BuiltInHabit.Workouts -> Icons.Outlined.FitnessCenter
    }
