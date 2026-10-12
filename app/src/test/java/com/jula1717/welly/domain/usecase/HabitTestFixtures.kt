package com.jula1717.welly.domain.usecase

import com.jula1717.welly.domain.model.BuiltInHabit
import com.jula1717.welly.domain.model.DailyTargets
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Habit
import com.jula1717.welly.domain.model.HabitDayLog
import com.jula1717.welly.domain.model.HabitFrequency
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.fake.FakeDrinkRepository
import com.jula1717.welly.fake.FakeHabitRepository
import com.jula1717.welly.fake.FakeMealRepository
import com.jula1717.welly.fake.FakeUserProfileRepository
import com.jula1717.welly.fake.FakeWorkoutRepository
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneOffset

internal val HABIT_TODAY: LocalDate = LocalDate.of(2026, 10, 15)
internal val HABIT_MONDAY: LocalDate = HABIT_TODAY.minusDays(3)

internal fun habitClock(date: LocalDate = HABIT_TODAY): Clock =
    Clock.fixed(date.atTime(12, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

internal fun testHabit(
    id: Long = 1L,
    builtIn: BuiltInHabit? = null,
    frequency: HabitFrequency = HabitFrequency.Daily,
    startDate: LocalDate = LocalDate.of(2026, 10, 1),
    isEnabled: Boolean = true,
) = Habit(
    id = id,
    builtIn = builtIn,
    name = if (builtIn == null) "Habit $id" else null,
    points = 10,
    frequency = frequency,
    isEnabled = isEnabled,
    startDate = startDate,
)

internal fun logsOn(vararg days: LocalDate): Map<LocalDate, HabitDayLog> = days.associateWith { HabitDayLog(count = 1) }

internal fun getHabitLogsUseCase(
    habits: FakeHabitRepository = FakeHabitRepository(),
    meals: List<Meal> = emptyList(),
    drinks: List<Drink> = emptyList(),
    workouts: List<Workout> = emptyList(),
    clock: Clock = habitClock(),
) = GetHabitLogsUseCase(
    habitRepository = habits,
    mealRepository = FakeMealRepository(meals),
    drinkRepository = FakeDrinkRepository(drinks),
    workoutRepository = FakeWorkoutRepository(workouts),
    observeDailyTargets =
        ObserveDailyTargetsUseCase(
            ObserveUserProfileUseCase(FakeUserProfileRepository()),
            CalculateDailyTargetsUseCase(CalculateBmrUseCase(), clock),
            clock,
        ),
    calculateBuiltInLogs = CalculateBuiltInHabitLogsUseCase(CalculateDailyIntakeTotalsUseCase()),
)

internal fun observeWorkoutGoalUseCase(profile: UserProfile? = null) =
    ObserveWorkoutGoalUseCase(ObserveUserProfileUseCase(FakeUserProfileRepository(profile)))

internal fun getHabitsUseCase(
    habits: FakeHabitRepository,
    profile: UserProfile? = null,
) = GetHabitsUseCase(habits, observeWorkoutGoalUseCase(profile))

internal fun getHabitUseCase(
    habits: FakeHabitRepository,
    profile: UserProfile? = null,
) = GetHabitUseCase(habits, observeWorkoutGoalUseCase(profile))

internal fun calculateHabitStatsUseCase(): CalculateHabitStatsUseCase {
    val getPeriod = GetHabitPeriodUseCase()
    return CalculateHabitStatsUseCase(getPeriod, CalculateHabitDayStatesUseCase(getPeriod))
}

internal fun getHabitSummariesUseCase(
    habits: FakeHabitRepository,
    clock: Clock = habitClock(),
    meals: List<Meal> = emptyList(),
    drinks: List<Drink> = emptyList(),
    workouts: List<Workout> = emptyList(),
): GetHabitSummariesUseCase {
    val getPeriod = GetHabitPeriodUseCase()
    return GetHabitSummariesUseCase(
        getHabits = getHabitsUseCase(habits),
        getHabitLogs = getHabitLogsUseCase(habits, meals, drinks, workouts, clock),
        getPeriod = getPeriod,
        calculateDayStates = CalculateHabitDayStatesUseCase(getPeriod),
        calculateStats = calculateHabitStatsUseCase(),
        clock = clock,
    )
}

internal fun getHabitStatsUseCase(
    habits: FakeHabitRepository,
    clock: Clock = habitClock(),
) = GetHabitStatsUseCase(
    getHabit = getHabitUseCase(habits),
    getHabitLogs = getHabitLogsUseCase(habits, clock = clock),
    calculateStats = calculateHabitStatsUseCase(),
    clock = clock,
)

internal fun testTargets() =
    DailyTargets(
        bmrKcal = 1500,
        tdeeKcal = 2000,
        calorieTargetKcalMin = 1800,
        calorieTargetKcalMax = 2200,
        proteinGramsMin = 100,
        proteinGramsMax = 140,
        carbsGramsMin = 200,
        carbsGramsMax = 300,
        fatGramsMin = 50,
        fatGramsMax = 80,
        fiberGramsMin = 25,
        fiberGramsMax = 35,
        hydrationMlMin = 2000,
        hydrationMlMax = 2800,
    )
