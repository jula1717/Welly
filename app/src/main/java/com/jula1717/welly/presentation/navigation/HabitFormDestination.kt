package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.habitform.HabitFormDestination
import kotlinx.serialization.Serializable

@Serializable
data class HabitFormDestination(
    val habitId: Long? = null,
) : WellyDestination {
    companion object {
        const val HABIT_ID_KEY = "habitId"
    }
}

fun NavController.navigateToHabitForm(habitId: Long? = null) {
    navigate(HabitFormDestination(habitId)) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.habitFormDestination(onBack: () -> Unit) {
    composable<HabitFormDestination> {
        HabitFormDestination(onBack = onBack)
    }
}
