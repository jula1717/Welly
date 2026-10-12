package com.jula1717.welly.domain.model

fun Habit.withWorkoutGoal(workoutsPerWeek: Int): Habit =
    if (builtIn == BuiltInHabit.Workouts) {
        copy(frequency = HabitFrequency(times = workoutsPerWeek, periodLength = 1, periodUnit = HabitPeriodUnit.Week))
    } else {
        this
    }
