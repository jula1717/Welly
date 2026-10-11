package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.DailyIntakeTotals
import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.HabitDayDetail
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.Workout
import java.time.LocalDate
import javax.inject.Inject

class CalculateBuiltInHabitLogsUseCase
    @Inject
    constructor(
        private val calculateTotals: CalculateDailyIntakeTotalsUseCase,
    ) {
        fun forWorkouts(workouts: List<Workout>): Map<LocalDate, HabitDayLog> =
            workouts
                .groupBy { it.dateTime.toLocalDate() }
                .mapValues { (_, dayWorkouts) ->
                    HabitDayLog(
                        count = dayWorkouts.size,
                        detail = HabitDayDetail.Workouts(dayWorkouts.map { it.activity.type }.toSet()),
                    )
                }

        /** [builtIn] must be one of the nutrition habits; workouts are handled by [forWorkouts]. */
        fun forNutrition(
            builtIn: BuiltInHabit,
            meals: List<Meal>,
            drinks: List<Drink>,
            targets: DailyTargets,
        ): Map<LocalDate, HabitDayLog> {
            require(builtIn != BuiltInHabit.Workouts) { "Workouts are not a nutrition habit" }
            val mealsByDay = meals.groupBy { it.dateTime.toLocalDate() }
            val drinksByDay = drinks.groupBy { it.dateTime.toLocalDate() }
            return (mealsByDay.keys + drinksByDay.keys)
                .filter { day ->
                    isMet(builtIn, calculateTotals(mealsByDay[day].orEmpty(), drinksByDay[day].orEmpty()), targets)
                }.associateWith { HabitDayLog(count = 1) }
        }

        private fun isMet(
            builtIn: BuiltInHabit,
            totals: DailyIntakeTotals,
            targets: DailyTargets,
        ): Boolean =
            when (builtIn) {
                BuiltInHabit.Calories -> totals.calories in targets.calorieTargetKcalMin..targets.calorieTargetKcalMax
                BuiltInHabit.Macros ->
                    totals.protein in targets.proteinGramsMin..targets.proteinGramsMax &&
                        totals.carbs in targets.carbsGramsMin..targets.carbsGramsMax &&
                        totals.fat in targets.fatGramsMin..targets.fatGramsMax
                BuiltInHabit.Hydration -> totals.hydrationMl >= targets.hydrationMlMin
                BuiltInHabit.Workouts -> false
            }
    }
