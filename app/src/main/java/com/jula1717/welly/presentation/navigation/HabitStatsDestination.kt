package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.habitstats.HabitStatsDestination
import kotlinx.serialization.Serializable

@Serializable
data class HabitStatsDestination(
    val habitId: Long,
) : WellyDestination {
    companion object {
        const val HABIT_ID_KEY = "habitId"
    }
}

fun NavController.navigateToHabitStats(habitId: Long) {
    navigate(HabitStatsDestination(habitId)) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.habitStatsDestination(
    onBack: () -> Unit,
    onEditHabit: (Long) -> Unit,
) {
    composable<HabitStatsDestination> {
        HabitStatsDestination(
            onBack = onBack,
            onEditHabit = onEditHabit,
        )
    }
}
