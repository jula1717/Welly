package com.jula1717.welly.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Today
import androidx.compose.ui.graphics.vector.ImageVector
import com.jula1717.welly.R

enum class BottomNavBarDestination(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    Today(
        route = Destinations.TODAY,
        labelRes = R.string.nav_today,
        icon = Icons.Outlined.Today,
    ),
    Stats(
        route = Destinations.STATS,
        labelRes = R.string.nav_stats,
        icon = Icons.Outlined.BarChart,
    ),
    Profile(
        route = Destinations.PROFILE,
        labelRes = R.string.nav_profile,
        icon = Icons.Outlined.Person,
    ),
}
