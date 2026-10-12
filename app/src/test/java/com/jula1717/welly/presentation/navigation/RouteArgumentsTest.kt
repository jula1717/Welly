package com.jula1717.welly.presentation.navigation

import org.junit.Assert.assertEquals
import org.junit.Test

/** The view models read their argument by key, so the key has to stay in sync with the route's property. */
class RouteArgumentsTest {
    @Test
    fun `habit stats route exposes the habit id under the documented key`() {
        val argumentName = HabitStatsDestination.serializer().descriptor.getElementName(0)

        assertEquals(HabitStatsDestination.HABIT_ID_KEY, argumentName)
    }

    @Test
    fun `habit form route exposes the habit id under the documented key`() {
        val argumentName = HabitFormDestination.serializer().descriptor.getElementName(0)

        assertEquals(HabitFormDestination.HABIT_ID_KEY, argumentName)
    }
}
