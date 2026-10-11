package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.repository.DrinkRepository
import com.jula1717.welly.domain.repository.HabitRepository
import com.jula1717.welly.domain.repository.MealRepository
import com.jula1717.welly.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetHabitLogsUseCase
    @Inject
    constructor(
        private val habitRepository: HabitRepository,
        private val mealRepository: MealRepository,
        private val drinkRepository: DrinkRepository,
        private val workoutRepository: WorkoutRepository,
        private val observeDailyTargets: ObserveDailyTargetsUseCase,
        private val calculateBuiltInLogs: CalculateBuiltInHabitLogsUseCase,
    ) {
        operator fun invoke(
            habit: Habit,
            range: ClosedRange<LocalDate>,
        ): Flow<Map<LocalDate, HabitDayLog>> {
            val builtIn = habit.builtIn ?: return customLogs(habit, range)
            return if (builtIn == BuiltInHabit.Workouts) {
                workoutRepository.getWorkoutsInRange(range).map(calculateBuiltInLogs::forWorkouts)
            } else {
                nutritionLogs(builtIn, range)
            }
        }

        private fun customLogs(
            habit: Habit,
            range: ClosedRange<LocalDate>,
        ): Flow<Map<LocalDate, HabitDayLog>> =
            habitRepository.getCompletions(habit.id, range.start, range.endInclusive).map { completions ->
                completions.associate { it.date to HabitDayLog(count = it.count) }
            }

        private fun nutritionLogs(
            builtIn: BuiltInHabit,
            range: ClosedRange<LocalDate>,
        ): Flow<Map<LocalDate, HabitDayLog>> =
            combine(
                mealRepository.getMealsInRange(range),
                drinkRepository.getDrinksInRange(range),
                observeDailyTargets(),
            ) { meals, drinks, targets ->
                calculateBuiltInLogs.forNutrition(builtIn, meals, drinks, targets)
            }
    }
