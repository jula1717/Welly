package com.jula1717.welly.presentation.today

import com.jula1717.welly.MainDispatcherRule
import com.jula1717.welly.domain.model.Activity
import com.jula1717.welly.domain.model.ActivityType
import com.jula1717.welly.domain.model.Drink
import com.jula1717.welly.domain.model.Meal
import com.jula1717.welly.domain.model.MealMacros
import com.jula1717.welly.domain.model.MealType
import com.jula1717.welly.domain.model.UserProfile
import com.jula1717.welly.domain.model.Workout
import com.jula1717.welly.domain.repository.DrinkRepository
import com.jula1717.welly.domain.repository.MealRepository
import com.jula1717.welly.domain.repository.UserProfileRepository
import com.jula1717.welly.domain.usecase.CalculateBmrUseCase
import com.jula1717.welly.domain.usecase.CalculateDailyIntakeTotalsUseCase
import com.jula1717.welly.domain.usecase.CalculateDailyTargetsUseCase
import com.jula1717.welly.domain.usecase.GetDrinksForDayUseCase
import com.jula1717.welly.domain.usecase.GetMealsForDayUseCase
import com.jula1717.welly.domain.usecase.GetWorkoutsForDayUseCase
import com.jula1717.welly.domain.usecase.ObserveUserProfileUseCase
import com.jula1717.welly.fake.FakeWorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class TodayViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val sep4 = LocalDate.of(2026, 9, 4)
    private val sep3 = sep4.minusDays(1)
    private val sep5 = sep4.plusDays(1)

    private val clock = MutableClock(sep4.atTime(12, 0).toInstant(ZoneOffset.UTC), ZoneOffset.UTC)

    private fun TestScope.buildViewModel(
        meals: List<Meal> = emptyList(),
        drinks: List<Drink> = emptyList(),
        workouts: List<Workout> = emptyList(),
        profile: UserProfile? = null,
    ): TodayViewModel {
        val viewModel = TodayViewModel(
            getMealsForDayUseCase = GetMealsForDayUseCase(FakeMealRepository(meals)),
            getDrinksForDayUseCase = GetDrinksForDayUseCase(FakeDrinkRepository(drinks)),
            getWorkoutsForDayUseCase = GetWorkoutsForDayUseCase(FakeWorkoutRepository(workouts)),
            observeUserProfileUseCase = ObserveUserProfileUseCase(FakeUserProfileRepository(profile)),
            calculateDailyTargetsUseCase = CalculateDailyTargetsUseCase(CalculateBmrUseCase(), clock),
            calculateDailyIntakeTotals = CalculateDailyIntakeTotalsUseCase(),
            clock = clock,
        )
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        return viewModel
    }

    @Test
    fun `initial state is anchored to today from the clock`() =
        runTest {
            val state = buildViewModel().uiState.value

            assertEquals(sep4, state.date)
            assertEquals(sep4, state.maxSelectableDate)
            assertFalse(state.canGoToNextDay)
        }

    @Test
    fun `previous day moves selection back and enables the next-day step`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(TodayUiEvent.OnPreviousDay)

            assertEquals(sep3, viewModel.uiState.value.date)
            assertTrue(viewModel.uiState.value.canGoToNextDay)
        }

    @Test
    fun `next day is clamped at today`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(TodayUiEvent.OnNextDay)

            assertEquals(sep4, viewModel.uiState.value.date)
        }

    @Test
    fun `next day advances from a past day`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(TodayUiEvent.OnPreviousDay)
            viewModel.onEvent(TodayUiEvent.OnPreviousDay)
            viewModel.onEvent(TodayUiEvent.OnNextDay)

            assertEquals(sep3, viewModel.uiState.value.date)
        }

    @Test
    fun `selecting a future date snaps back to today`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(TodayUiEvent.OnDateSelected(sep4.plusDays(7)))

            assertEquals(sep4, viewModel.uiState.value.date)
        }

    @Test
    fun `selecting a past date is accepted`() =
        runTest {
            val viewModel = buildViewModel()

            viewModel.onEvent(TodayUiEvent.OnDateSelected(sep4.minusDays(10)))

            assertEquals(sep4.minusDays(10), viewModel.uiState.value.date)
        }

    @Test
    fun `entries and totals reflect the selected day`() =
        runTest {
            val viewModel = buildViewModel(
                meals = listOf(
                    meal(id = 1, at = sep4.atTime(8, 0), calories = 500),
                    meal(id = 2, at = sep3.atTime(8, 0), calories = 999),
                ),
            )

            val todayEntries = viewModel.uiState.value.nutritionEntries
            assertEquals(1, todayEntries.size)
            assertEquals("meal-1", todayEntries.first().key)
            assertEquals(500, viewModel.uiState.value.totals.calories)

            viewModel.onEvent(TodayUiEvent.OnPreviousDay)

            val yesterdayEntries = viewModel.uiState.value.nutritionEntries
            assertEquals(1, yesterdayEntries.size)
            assertEquals("meal-2", yesterdayEntries.first().key)
            assertEquals(999, viewModel.uiState.value.totals.calories)
        }

    @Test
    fun `workouts of the selected day are listed separately without changing totals`() =
        runTest {
            val jogging = Activity(id = 1, name = "Jogging", type = ActivityType.Cardio)
            val viewModel = buildViewModel(
                meals = listOf(meal(id = 1, at = sep4.atTime(8, 0), calories = 500)),
                workouts = listOf(
                    Workout(id = 1, activity = jogging, dateTime = sep4.atTime(18, 0)),
                    Workout(id = 2, activity = jogging, dateTime = sep3.atTime(18, 0)),
                ),
            )

            val state = viewModel.uiState.value
            assertEquals(listOf("meal-1"), state.nutritionEntries.map { it.key })
            assertEquals(listOf("workout-1"), state.workoutEntries.map { it.key })
            assertEquals(500, state.totals.calories)
        }

    @Test
    fun `onScreenResumed after midnight refreshes today without moving the user`() =
        runTest {
            val viewModel = buildViewModel()
            assertFalse(viewModel.uiState.value.canGoToNextDay)

            clock.instant = sep5.atTime(1, 0).toInstant(ZoneOffset.UTC)
            viewModel.onScreenResumed()

            val state = viewModel.uiState.value
            assertEquals(sep4, state.date)
            assertEquals(sep5, state.maxSelectableDate)
            assertTrue(state.canGoToNextDay)
        }

    private fun meal(
        id: Long,
        at: LocalDateTime,
        calories: Int,
    ) = Meal(
        id = id,
        dateTime = at,
        type = MealType.Lunch,
        description = "meal $id",
        macros = MealMacros(calories = calories, protein = 0, carbs = 0, fat = 0, fiber = 0),
    )

    private class MutableClock(
        var instant: Instant,
        private val zone: ZoneId,
    ) : Clock() {
        override fun getZone(): ZoneId = zone

        override fun withZone(zone: ZoneId): Clock = MutableClock(instant, zone)

        override fun instant(): Instant = instant
    }

    private class FakeMealRepository(
        private val meals: List<Meal>,
    ) : MealRepository {
        override suspend fun addMeal(meal: Meal) = Unit

        override fun getMealsForDay(date: LocalDate): Flow<List<Meal>> =
            flowOf(meals.filter { it.dateTime.toLocalDate() == date })
    }

    private class FakeDrinkRepository(
        private val drinks: List<Drink>,
    ) : DrinkRepository {
        override suspend fun addDrink(drink: Drink) = Unit

        override fun getDrinksForDay(date: LocalDate): Flow<List<Drink>> =
            flowOf(drinks.filter { it.dateTime.toLocalDate() == date })
    }

    private class FakeUserProfileRepository(
        private val profile: UserProfile?,
    ) : UserProfileRepository {
        override fun observeProfile(): Flow<UserProfile?> = flowOf(profile)

        override suspend fun updateProfile(profile: UserProfile) = Unit
    }
}
