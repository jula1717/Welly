package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.addmeal.AddMealDestination
import kotlinx.serialization.Serializable

@Serializable
data object AddMealDestination : WellyDestination

fun NavController.navigateToAddMeal() {
    navigate(AddMealDestination)
}

fun NavGraphBuilder.addMealDestination(
    onBack: () -> Unit
) {
    composable<AddMealDestination> {
        AddMealDestination(
            onBack = onBack
        )
    }
}
