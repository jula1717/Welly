package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.adddrink.AddDrinkDestination
import kotlinx.serialization.Serializable

@Serializable
data object AddDrinkDestination : WellyDestination

fun NavController.navigateToAddDrink() {
    navigate(AddDrinkDestination) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.addDrinkDestination(onBack: () -> Unit) {
    composable<AddDrinkDestination> {
        AddDrinkDestination(
            onBack = onBack,
        )
    }
}
