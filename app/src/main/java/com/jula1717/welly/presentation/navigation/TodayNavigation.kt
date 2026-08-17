package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.today.TodayDestination
import kotlinx.serialization.Serializable

@Serializable
data object TodayDestination : WellyDestination

fun NavController.navigateToToday() {
    navigate(TodayDestination)
}

fun NavGraphBuilder.todayDestination(
    onAddMeal: () -> Unit,
    onAddDrink: () -> Unit,
) {
    composable<TodayDestination> {
        TodayDestination(
            onAddMeal = onAddMeal,
            onAddDrink = onAddDrink,
        )
    }
}
