package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.settings.BuiltInHabitsDestination
import kotlinx.serialization.Serializable

@Serializable
data object BuiltInHabitsDestination : WellyDestination

fun NavController.navigateToBuiltInHabits() {
    navigate(BuiltInHabitsDestination) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.builtInHabitsDestination(onBack: () -> Unit) {
    composable<BuiltInHabitsDestination> {
        BuiltInHabitsDestination(onBack = onBack)
    }
}
