package com.jula1717.welly.presentation.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlin.reflect.KClass

private val formDestinations: Set<KClass<out WellyDestination>> = setOf(
    AddMealDestination::class,
    AddDrinkDestination::class,
)

fun isFormDestination(destination: NavDestination?): Boolean =
    formDestinations.any { destination?.hasRoute(it) == true }
