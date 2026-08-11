package com.jula1717.welly.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Today
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R

enum class BottomNavBarDestination(
    val route: WellyDestination,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    Profile(
        route = ProfileDestination,
        labelRes = R.string.nav_profile,
        icon = Icons.Outlined.Person,
    ),
    Today(
        route = TodayDestination,
        labelRes = R.string.nav_today,
        icon = Icons.Outlined.Today,
    ),
    Stats(
        route = StatsDestination,
        labelRes = R.string.nav_stats,
        icon = Icons.Outlined.BarChart,
    ),
}
