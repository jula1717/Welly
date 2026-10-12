package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.habits.HabitsDestination
import kotlinx.serialization.Serializable

@Serializable
data object HabitsDestination : WellyDestination

fun NavGraphBuilder.habitsDestination(
    onOpenHabit: (Long) -> Unit,
    onAddHabit: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    composable<HabitsDestination> {
        HabitsDestination(
            onOpenHabit = onOpenHabit,
            onAddHabit = onAddHabit,
            onOpenProfile = onOpenProfile,
            onOpenSettings = onOpenSettings,
        )
    }
}
