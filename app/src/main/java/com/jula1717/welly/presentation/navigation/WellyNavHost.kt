package com.jula1717.welly.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun WellyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = !isFormDestination(currentDestination)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                WellyBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TodayDestination,
            modifier = Modifier.padding(innerPadding),
        ) {
            wellyGraph(navController)
        }
    }
}

private fun NavGraphBuilder.wellyGraph(navController: NavHostController) {
    profileDestination(
        onBack = { navController.popBackStack() },
    )

    todayDestination(
        onAddMeal = { navController.navigateToAddMeal() },
        onAddDrink = { navController.navigateToAddDrink() },
        onAddWorkout = { navController.navigateToAddWorkout() },
        onOpenProfile = { navController.navigateToProfile() },
        onOpenSettings = { navController.navigateToSettings() },
    )

    settingsDestination(
        onBack = { navController.popBackStack() },
        onOpenProfile = { navController.navigateToProfile() },
        onOpenActivities = { navController.navigateToActivities() },
        onOpenBuiltInHabits = { navController.navigateToBuiltInHabits() },
    )

    builtInHabitsDestination(
        onBack = { navController.popBackStack() },
    )

    activitiesDestination(
        onBack = { navController.popBackStack() },
    )

    addWorkoutDestination(
        onBack = { navController.popBackStack() },
    )

    addMealDestination(
        onBack = { navController.popBackStack() },
    )

    addDrinkDestination(
        onBack = { navController.popBackStack() },
    )

    habitsDestination(
        onOpenHabit = { habitId -> navController.navigateToHabitStats(habitId) },
        onAddHabit = { navController.navigateToHabitForm() },
        onOpenProfile = { navController.navigateToProfile() },
        onOpenSettings = { navController.navigateToSettings() },
    )

    habitStatsDestination(
        onBack = { navController.popBackStack() },
        onEditHabit = { habitId -> navController.navigateToHabitForm(habitId) },
    )

    habitFormDestination(
        onBack = { navController.popBackStack() },
    )
}
