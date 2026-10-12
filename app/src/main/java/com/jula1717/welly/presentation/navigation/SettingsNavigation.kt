package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.settings.SettingsDestination
import kotlinx.serialization.Serializable

@Serializable
data object SettingsDestination : WellyDestination

fun NavController.navigateToSettings() {
    navigate(SettingsDestination) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.settingsDestination(
    onBack: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenActivities: () -> Unit,
    onOpenBuiltInHabits: () -> Unit,
) {
    composable<SettingsDestination> {
        SettingsDestination(
            onBack = onBack,
            onOpenProfile = onOpenProfile,
            onOpenActivities = onOpenActivities,
            onOpenBuiltInHabits = onOpenBuiltInHabits,
        )
    }
}
