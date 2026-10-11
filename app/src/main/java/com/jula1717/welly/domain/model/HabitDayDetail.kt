package com.jula1717.welly.domain.model

/** Extra information about a habit's day that only some habits have. */
sealed interface HabitDayDetail {
    data object None : HabitDayDetail

    /** Kinds of activity done on a workouts day, so the calendar can show cardio / strength. */
    data class Workouts(
        val activityTypes: Set<ActivityType>,
    ) : HabitDayDetail
}
