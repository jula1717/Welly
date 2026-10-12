package com.jula1717.welly.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R

enum class BottomNavBarDestination(
    val route: WellyDestination,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    //TODO: rename today -> log
    Log(
        route = TodayDestination,
        labelRes = R.string.nav_log,
        icon = Icons.Outlined.EditNote,
    ),
    Habits(
        route = HabitsDestination,
        labelRes = R.string.nav_habits,
        icon = Icons.Outlined.TaskAlt,
    ),
}
