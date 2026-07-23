package com.jula1717.welly.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jula1717.welly.presentation.profile.ProfileScreen
import com.jula1717.welly.presentation.stats.StatsScreen
import com.jula1717.welly.presentation.today.TodayScreen

@Composable
fun WellyNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        modifier = modifier,
        bottomBar = {
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
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.TODAY,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Destinations.TODAY) {
                TodayScreen()
            }
            composable(Destinations.STATS) {
                StatsScreen()
            }
            composable(Destinations.PROFILE) {
                ProfileScreen()
            }
        }
    }
}
