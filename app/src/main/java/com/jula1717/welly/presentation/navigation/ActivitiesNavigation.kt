package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.activities.ActivitiesDestination
import kotlinx.serialization.Serializable

@Serializable
data object ActivitiesDestination : WellyDestination

fun NavController.navigateToActivities() {
    navigate(ActivitiesDestination) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.activitiesDestination(onBack: () -> Unit) {
    composable<ActivitiesDestination> {
        ActivitiesDestination(onBack = onBack)
    }
}
