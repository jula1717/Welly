package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jula1717.welly.presentation.profile.ProfileDestination
import kotlinx.serialization.Serializable

@Serializable
data object ProfileDestination : WellyDestination

fun NavController.navigateToProfile() {
    navigate(ProfileDestination)
}

fun NavGraphBuilder.profileDestination(
) {
    composable<ProfileDestination> {
        ProfileDestination()
    }
}
