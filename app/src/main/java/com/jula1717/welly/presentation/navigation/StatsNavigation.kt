package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.stats.StatsDestination
import kotlinx.serialization.Serializable

@Serializable
data object StatsDestination : WellyDestination

fun NavController.navigateToStats() {
    navigate(StatsDestination)
}

fun NavGraphBuilder.statsDestination() {
    composable<StatsDestination> {
        StatsDestination()
    }
}
